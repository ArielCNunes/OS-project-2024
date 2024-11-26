import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {
    // Fields
    private final Socket socket;
    private ReportManager sharedReportManager;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String message;
    private int optionChosen;

    // Constructor
    public ServerThread(Socket socket, ReportManager reportManager) {
        this.socket = socket;
        this.sharedReportManager = reportManager;
    }

    // Run() method
    public void run() {
        try {
            // Initialize output stream to send data to client
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush(); // Ensure the stream is clear before use
            // Initialize input stream to receive data from client
            in = new ObjectInputStream(socket.getInputStream());

            // Begin communication with the client
            do {
                do {
                    // Menu (for users not logged in)
                    sendMessage("1. Register new user"
                            + "\n2. Login to the Health and Safety Reporting System");

                    // Get input and parse it
                    message = (String) in.readObject();
                    optionChosen = Integer.parseInt(message);
                } while ((optionChosen != 1) && (optionChosen != 2));

                if (message.equalsIgnoreCase("1")) { // Register

                } else if (message.equalsIgnoreCase("2")) { // Log in

                }

                // Ask client if they want to repeat
                sendMessage("Enter 1 to repeat");
                message = (String) in.readObject();
            } while (message.equalsIgnoreCase("1"));

        } catch (IOException e) { // Catch IOException for communication errors
            e.printStackTrace();
        } catch (ClassNotFoundException e) { // Catch exception if received object is of unknown type
            e.printStackTrace();
        } finally {
            // Closing connections in finally block to ensure resources are released
            try {
                in.close();
                out.close();
                socket.close(); // Close socket connection
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // Helper method to send a message to the client
    void sendMessage(String msg) {
        try {
            out.writeObject(msg); // Send message as an object
            out.flush(); // Clear the stream after sending
            System.out.println("server> " + msg); // Log message to server console
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
