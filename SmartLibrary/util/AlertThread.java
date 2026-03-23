package SmartLibrary.util;

import SmartLibrary.controller.LibraryController;
import SmartLibrary.model.IssueRecord;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.List;

/**
 * Background thread that checks overdue records and raises popup alerts.
 */
public class AlertThread extends Thread {
    private final LibraryController libraryController;
    private volatile boolean running = true;

    public AlertThread(LibraryController libraryController) {
        this.libraryController = libraryController;
        setName("OverdueAlertThread");
        setDaemon(true);
    }

    @Override
    public void run() {
        while (running) {
            List<IssueRecord> overdue = libraryController.getOverdueRecords();
            if (!overdue.isEmpty()) {
                StringBuilder message = new StringBuilder("Overdue books detected:\n");
                for (IssueRecord record : overdue) {
                    message.append("Student ")
                            .append(record.getStudentId())
                            .append(" -> Book ")
                            .append(record.getBookId())
                            .append(" | Fine Rs. ")
                            .append(libraryController.calculateFine(record.getDaysLate()))
                            .append('\n');
                }
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message.toString()));
            }

            try {
                Thread.sleep(15_000L);
            } catch (InterruptedException ex) {
                interrupt();
                running = false;
            }
        }
    }

    public void shutdown() {
        running = false;
        interrupt();
    }
}
