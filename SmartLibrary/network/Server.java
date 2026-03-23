package SmartLibrary.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Receives shared library records from other systems.
 */
public class Server {
    public void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Library server started on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket), "LibraryClientHandler").start();
            }
        } catch (IOException ex) {
            System.err.println("Server error: " + ex.getMessage());
        }
    }

    private void handleClient(Socket socket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Received shared record: " + line);
            }
        } catch (IOException ex) {
            System.err.println("Client handler error: " + ex.getMessage());
        }
    }
}
