package SmartLibrary.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * Sends library records to another machine over sockets.
 */
public class Client {
    public void sendRecords(String host, int port, List<String> records) {
        try (Socket socket = new Socket(host, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            for (String record : records) {
                writer.println(record);
            }
            System.out.println("Records sent successfully.");
        } catch (IOException ex) {
            System.err.println("Client error: " + ex.getMessage());
        }
    }
}
