package SmartLibrary.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Captures issue and return information for a book.
 */
public class IssueRecord {
    private int studentId;
    private int bookId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public IssueRecord(int studentId, int bookId, LocalDate issueDate, LocalDate dueDate, LocalDate returnDate) {
        this.studentId = studentId;
        this.bookId = bookId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getBookId() {
        return bookId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public int getDaysLate() {
        LocalDate comparisonDate = returnDate != null ? returnDate : LocalDate.now();
        if (comparisonDate.isAfter(dueDate)) {
            return (int) ChronoUnit.DAYS.between(dueDate, comparisonDate);
        }
        return 0;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    @Override
    public String toString() {
        return studentId + "," + bookId + "," + issueDate + "," + dueDate + "," + (returnDate == null ? "" : returnDate);
    }
}
