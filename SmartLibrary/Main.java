package SmartLibrary;

import SmartLibrary.network.Server;
import SmartLibrary.view.LoginFrame;

import javax.swing.SwingUtilities;

/**
 * Application entry point.
 */
public class Main {
    public static void main(String[] args) {
        new Thread(() -> new Server().startServer(5050), "LibraryServer").start();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
