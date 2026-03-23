package SmartLibrary.view;

import SmartLibrary.controller.LibraryController;
import SmartLibrary.model.Book;
import SmartLibrary.network.Client;
import SmartLibrary.util.AlertThread;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.stream.Collectors;

/**
 * Main application dashboard.
 */
public class DashboardFrame extends JFrame {
    private final LibraryController libraryController = new LibraryController();
    private final LibraryPanel libraryPanel = new LibraryPanel(libraryController);
    private final AlertThread alertThread = new AlertThread(libraryController);

    public DashboardFrame() {
        setTitle("Smart Library Assistant Pro - Dashboard");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeUi();
        alertThread.start();
    }

    private void initializeUi() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBookButton = new JButton("Add Book");
        JButton issueBookButton = new JButton("Issue Book");
        JButton returnBookButton = new JButton("Return Book");
        JButton shareButton = new JButton("Share Records");

        addBookButton.addActionListener(event -> addBook());
        issueBookButton.addActionListener(event -> libraryPanel.issueSelectedBook());
        returnBookButton.addActionListener(event -> libraryPanel.returnSelectedBook());
        shareButton.addActionListener(event -> shareBooks());

        buttonPanel.add(addBookButton);
        buttonPanel.add(issueBookButton);
        buttonPanel.add(returnBookButton);
        buttonPanel.add(shareButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(libraryPanel, BorderLayout.CENTER);
    }

    private void addBook() {
        String idText = JOptionPane.showInputDialog(this, "Enter Book ID:");
        String title = JOptionPane.showInputDialog(this, "Enter Book Title:");
        String author = JOptionPane.showInputDialog(this, "Enter Author:");
        String category = JOptionPane.showInputDialog(this, "Enter Category:");
        if (idText != null && title != null && author != null && category != null) {
            Book book = new Book(Integer.parseInt(idText), title, author, category, true);
            libraryController.addBook(book);
            libraryPanel.refreshTable(libraryController.getBooks());
        }
    }

    private void shareBooks() {
        Client client = new Client();
        client.sendRecords("localhost", 5050,
                libraryController.getBooks().stream().map(Book::toString).collect(Collectors.toList()));
        JOptionPane.showMessageDialog(this, "Records shared with remote system.");
    }

    @Override
    public void dispose() {
        alertThread.shutdown();
        super.dispose();
    }
}
