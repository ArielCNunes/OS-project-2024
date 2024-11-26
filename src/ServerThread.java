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
    private int result;

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
                    // Send menu options to the client
                    sendMessage("MENU");
                    message = (String) in.readObject(); // Receive client input
                    result = Integer.parseInt(message); // Parse input to integer

                } while ((result != 1) && (result != 2) && (result != 3) && (result != 4)); // Repeat until valid input

                if (message.equalsIgnoreCase("1")) {


                } else if (message.equalsIgnoreCase("2")) {

                } else if (message.equalsIgnoreCase("3")) {

                } else if (message.equalsIgnoreCase("4")) {

                }

            } while (message.equalsIgnoreCase("1")); // Repeat if client enters 1

        } catch (IOException e) { // Catch IOException for communication errors
            e.printStackTrace();
        } catch (ClassNotFoundException e) { // Catch exception if received object is of unknown type
            e.printStackTrace();
        } finally {
            // Closing connections in finally block to ensure resources are released
            try {
                in.close(); // Close input stream
                out.close(); // Close output stream
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
            System.out.println("server>" + msg); // Log message to server console
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
