package SmartLibrary.controller;

import SmartLibrary.database.DBConnection;
import SmartLibrary.model.Book;
import SmartLibrary.model.IssueRecord;
import SmartLibrary.model.LibraryOperations;
import SmartLibrary.util.FileHandler;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Coordinates library business operations between the UI and persistence layers.
 */
public class LibraryController implements LibraryOperations {
    private static final double DAILY_FINE = 5.0;

    private final FileHandler fileHandler;
    private final List<Book> books;
    private final List<IssueRecord> issueRecords;

    public LibraryController() {
        this.fileHandler = new FileHandler();
        this.books = new ArrayList<>();
        this.issueRecords = new ArrayList<>();
        loadLocalData();
    }

    private void loadLocalData() {
        try {
            books.addAll(fileHandler.loadBooks());
            issueRecords.addAll(fileHandler.loadIssueRecords());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public List<IssueRecord> getIssueRecords() {
        return new ArrayList<>(issueRecords);
    }

    public void addBook(Book book) {
        books.add(book);
        persistBooks();
        insertBookToDatabase(book);
    }

    public List<Book> searchBooks(String keyword) {
        String search = keyword.toLowerCase();
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(search))
                .collect(Collectors.toList());
    }

    @Override
    public void issueBook(int studentId, int bookId) {
        Optional<Book> bookOptional = books.stream()
                .filter(book -> book.getId() == bookId && book.isAvailable())
                .findFirst();

        if (bookOptional.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Book not available for issue.");
            return;
        }

        Book book = bookOptional.get();
        book.setAvailable(false);
        IssueRecord record = new IssueRecord(studentId, bookId, LocalDate.now(), LocalDate.now().plusDays(7), null);
        issueRecords.add(record);
        persistBooks();
        persistIssues();
        insertIssueRecord(record);
        updateBookAvailability(bookId, false);
    }

    @Override
    public void returnBook(int studentId, int bookId) {
        for (IssueRecord record : issueRecords) {
            if (record.getStudentId() == studentId && record.getBookId() == bookId && !record.isReturned()) {
                record.setReturnDate(LocalDate.now());
                books.stream()
                        .filter(book -> book.getId() == bookId)
                        .findFirst()
                        .ifPresent(book -> book.setAvailable(true));
                persistBooks();
                persistIssues();
                updateReturnRecord(record);
                updateBookAvailability(bookId, true);
                int daysLate = record.getDaysLate();
                if (daysLate > 0) {
                    JOptionPane.showMessageDialog(null,
                            "Book returned late. Fine: Rs. " + calculateFine(daysLate));
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Matching issue record not found.");
    }

    @Override
    public double calculateFine(int daysLate) {
        return daysLate * DAILY_FINE;
    }

    public List<IssueRecord> getOverdueRecords() {
        return issueRecords.stream()
                .filter(record -> !record.isReturned() && record.getDueDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    private void persistBooks() {
        try {
            fileHandler.saveBooks(books);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void persistIssues() {
        try {
            fileHandler.saveIssueRecords(issueRecords);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void insertBookToDatabase(Book book) {
        String sql = "INSERT INTO books(id, title, author, category, available) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, book.getId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getCategory());
            statement.setBoolean(5, book.isAvailable());
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void insertIssueRecord(IssueRecord record) {
        String sql = "INSERT INTO issue_records(student_id, book_id, issue_date, due_date, return_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, record.getStudentId());
            statement.setInt(2, record.getBookId());
            statement.setDate(3, Date.valueOf(record.getIssueDate()));
            statement.setDate(4, Date.valueOf(record.getDueDate()));
            statement.setDate(5, null);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateReturnRecord(IssueRecord record) {
        String sql = "UPDATE issue_records SET return_date = ? WHERE student_id = ? AND book_id = ? AND return_date IS NULL";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, Date.valueOf(record.getReturnDate()));
            statement.setInt(2, record.getStudentId());
            statement.setInt(3, record.getBookId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateBookAvailability(int bookId, boolean available) {
        String sql = "UPDATE books SET available = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, available);
            statement.setInt(2, bookId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void syncBooksFromDatabase() {
        String sql = "SELECT id, title, author, category, available FROM books";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            books.clear();
            while (resultSet.next()) {
                books.add(new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("category"),
                        resultSet.getBoolean("available")
                ));
            }
            persistBooks();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
