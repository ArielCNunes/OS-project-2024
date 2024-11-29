import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Requester {
    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    Scanner input;

    // Constructor
    Requester() {
        input = new Scanner(System.in);
    }

    // Main method to run the client
    void run() {
        try {
            // 1. Creating a socket to connect to the server
            requestSocket = new Socket("127.0.0.1", 2004);
            System.out.println("Connected to localhost in port 2004");

            // 2. Getting Input and Output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());

            // 3: Communicating with the server
            do {
                do {
                    // Receiving menu options from server and displaying them to the user
                    message = (String) in.readObject();
                    System.out.println(message);

                    // User inputs their choice
                    message = input.nextLine();
                    sendMessage(message); // Send user choice to the server
                } while (!message.equalsIgnoreCase("1")
                        && !message.equalsIgnoreCase("2"));

                // Registration and log in
                if (message.equalsIgnoreCase("1")) {
                    // Receive and send user details
                    System.out.println((String) in.readObject()); // username
                    String username = input.nextLine();
                    sendMessage(username);

                    System.out.println((String) in.readObject()); // email
                    String email = input.nextLine();
                    sendMessage(email);

                    System.out.println((String) in.readObject()); // password
                    String password = input.nextLine();
                    sendMessage(password);

                    System.out.println((String) in.readObject()); // department
                    String department = input.nextLine();
                    sendMessage(department);

                    System.out.println((String) in.readObject()); // role
                    String role = input.nextLine();
                    sendMessage(role);

                    System.out.println((String) in.readObject()); // id
                    String id = input.nextLine();
                    sendMessage(id);

                    // Registration success or failure message
                    System.out.println((String) in.readObject());
                } else if (message.equalsIgnoreCase("2")) {
                    // Receive and send user login details
                    System.out.println((String) in.readObject()); // Email
                    String email = input.nextLine();
                    sendMessage(email);

                    System.out.println((String) in.readObject()); // Password
                    String password = input.nextLine();
                    sendMessage(password);

                    // Login success or failure message
                    System.out.println((String) in.readObject());
                }

                // Receive prompt to repeat the process
                message = (String) in.readObject();
                System.out.println(message);
                message = input.nextLine();
                sendMessage(message);
            } while (message.equalsIgnoreCase("1"));

        } catch (UnknownHostException unknownHost) { // Handle unknown host error
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) { // Handle input/output exceptions
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) { // Handle error if class of received object is unknown
            e.printStackTrace();
        } finally {
            // 4: Closing connection
            try {
                in.close(); // Close input stream
                out.close(); // Close output stream
                requestSocket.close(); // Close socket connection
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // Method to send message to server
    void sendMessage(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
            System.out.println("client>" + msg);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // Main method to start client application
    public static void main(String args[]) {
        Requester client = new Requester(); // Create client instance
        client.run(); // Run the client application
    }
}