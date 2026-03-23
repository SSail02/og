package SmartLibrary.model;

public interface LibraryOperations {
    void issueBook(int studentId, int bookId);
    void returnBook(int studentId, int bookId);
    double calculateFine(int daysLate);
}
