package SmartLibrary.util;

import SmartLibrary.model.Book;
import SmartLibrary.model.IssueRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists library records in CSV files.
 */
public class FileHandler {
    private final Path bookFile = Paths.get("SmartLibrary", "data", "books.csv");
    private final Path issueFile = Paths.get("SmartLibrary", "data", "issues.csv");

    public FileHandler() {
        try {
            Files.createDirectories(bookFile.getParent());
            if (Files.notExists(bookFile)) {
                Files.writeString(bookFile, "id,title,author,category,available\n");
            }
            if (Files.notExists(issueFile)) {
                Files.writeString(issueFile, "studentId,bookId,issueDate,dueDate,returnDate\n");
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to initialize storage files", ex);
        }
    }

    public synchronized void saveBooks(List<Book> books) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(bookFile)) {
            writer.write("id,title,author,category,available\n");
            for (Book book : books) {
                writer.write(book.toString());
                writer.newLine();
            }
        }
    }

    public synchronized List<Book> loadBooks() throws IOException {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(bookFile)) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    books.add(new Book(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            parts[2],
                            parts[3],
                            Boolean.parseBoolean(parts[4])
                    ));
                }
            }
        }
        return books;
    }

    public synchronized void saveIssueRecords(List<IssueRecord> records) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(issueFile)) {
            writer.write("studentId,bookId,issueDate,dueDate,returnDate\n");
            for (IssueRecord record : records) {
                writer.write(record.toString());
                writer.newLine();
            }
        }
    }

    public synchronized List<IssueRecord> loadIssueRecords() throws IOException {
        List<IssueRecord> records = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(issueFile)) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 5) {
                    records.add(new IssueRecord(
                            Integer.parseInt(parts[0]),
                            Integer.parseInt(parts[1]),
                            LocalDate.parse(parts[2]),
                            LocalDate.parse(parts[3]),
                            parts[4].isBlank() ? null : LocalDate.parse(parts[4])
                    ));
                }
            }
        }
        return records;
    }
}
