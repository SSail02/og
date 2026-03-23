package SmartLibrary.view;

import SmartLibrary.controller.LibraryController;
import SmartLibrary.model.Book;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

/**
 * Main book listing and actions panel.
 */
public class LibraryPanel extends JPanel {
    private final LibraryController libraryController;
    private final DefaultTableModel tableModel;
    private final JTable bookTable;
    private final JTextField searchField = new JTextField();

    public LibraryPanel(LibraryController libraryController) {
        this.libraryController = libraryController;
        this.tableModel = new DefaultTableModel(new Object[]{"ID", "Title", "Author", "Category", "Available"}, 0);
        this.bookTable = new JTable(tableModel);
        setLayout(new BorderLayout(10, 10));
        initializeUi();
        refreshTable(libraryController.getBooks());
    }

    private void initializeUi() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        JButton searchButton = new JButton("Search Book");
        searchButton.addActionListener(event -> refreshTable(libraryController.searchBooks(searchField.getText())));
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton issueButton = new JButton("Issue");
        JButton returnButton = new JButton("Return");
        JButton fineButton = new JButton("View Fine");

        issueButton.addActionListener(event -> issueSelectedBook());
        returnButton.addActionListener(event -> returnSelectedBook());
        fineButton.addActionListener(event -> showFineForSelectedBook());

        actionPanel.add(issueButton);
        actionPanel.add(returnButton);
        actionPanel.add(fineButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(bookTable), BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    public void refreshTable(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getId(), book.getTitle(), book.getAuthor(), book.getCategory(), book.isAvailable()});
        }
    }

    public void issueSelectedBook() {
        int row = bookTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a book first.");
            return;
        }
        int bookId = (int) tableModel.getValueAt(row, 0);
        String studentIdText = JOptionPane.showInputDialog(this, "Enter Student ID:");
        if (studentIdText != null && !studentIdText.isBlank()) {
            libraryController.issueBook(Integer.parseInt(studentIdText), bookId);
            refreshTable(libraryController.getBooks());
        }
    }

    public void returnSelectedBook() {
        int row = bookTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a book first.");
            return;
        }
        int bookId = (int) tableModel.getValueAt(row, 0);
        String studentIdText = JOptionPane.showInputDialog(this, "Enter Student ID:");
        if (studentIdText != null && !studentIdText.isBlank()) {
            libraryController.returnBook(Integer.parseInt(studentIdText), bookId);
            refreshTable(libraryController.getBooks());
        }
    }

    private void showFineForSelectedBook() {
        int row = bookTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a book first.");
            return;
        }
        int bookId = (int) tableModel.getValueAt(row, 0);
        libraryController.getIssueRecords().stream()
                .filter(record -> record.getBookId() == bookId)
                .findFirst()
                .ifPresentOrElse(record -> JOptionPane.showMessageDialog(this,
                                "Current fine: Rs. " + libraryController.calculateFine(record.getDaysLate())),
                        () -> JOptionPane.showMessageDialog(this, "No issue history found for this book."));
    }
}
