import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    public static void main(String args[]) {
        ServerSocket provider;
        Socket connection;
        ServerThread handler;
        ReportManager reportManager = new ReportManager();

        try {
            // Server socket on port 2004 with a maximum queue of 10 clients
            provider = new ServerSocket(2004, 10);

            // Keep the server running and accepting connections
            while (true) {
                // Accept a client connection
                connection = provider.accept();

                // Create a new thread for handling the client connection
                handler = new ServerThread(connection, reportManager);

                // Start the thread to process client requests concurrently
                handler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}