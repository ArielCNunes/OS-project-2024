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
                    // Log in or register options
                    message = (String) in.readObject();
                    System.out.println(message);

                    // Get input and send to server
                    message = input.nextLine();
                    sendMessage(message);
                } while (!message.equalsIgnoreCase("1")
                        && !message.equalsIgnoreCase("2"));

                // Register option
                if (message.equalsIgnoreCase("1")) {
                    // username
                    System.out.println((String) in.readObject());
                    String username = input.nextLine();
                    sendMessage(username);

                    // email
                    System.out.println((String) in.readObject());
                    String email = input.nextLine();
                    sendMessage(email);

                    // password
                    System.out.println((String) in.readObject());
                    String password = input.nextLine();
                    sendMessage(password);

                    // department
                    System.out.println((String) in.readObject());
                    String department = input.nextLine();
                    sendMessage(department);

                    // role
                    System.out.println((String) in.readObject());
                    String role = input.nextLine();
                    sendMessage(role);

                    // id
                    System.out.println((String) in.readObject());
                    String id = input.nextLine();
                    sendMessage(id);

                    // Registration success or failure message
                    System.out.println((String) in.readObject());

                    // Log in option
                } else if (message.equalsIgnoreCase("2")) {
                    // Email
                    System.out.println((String) in.readObject());
                    String email = input.nextLine();
                    sendMessage(email);

                    // Password
                    System.out.println((String) in.readObject());
                    String password = input.nextLine();
                    sendMessage(password);

                    // Login success or failure message
                    System.out.println((String) in.readObject());

                    // Options 3 - 7 for authenticated users
                    String userChoice;
                    do {
                        // Receive, display options 3-7, and send answer
                        System.out.println((String) in.readObject());
                        userChoice = input.nextLine();
                        sendMessage(userChoice);

                        // Execute one of the option
                        switch (userChoice) {
                            // Create report
                            case "3":
                                // report type
                                System.out.println((String) in.readObject());
                                String reportType = input.nextLine();
                                sendMessage(reportType);

                                // description
                                System.out.println((String) in.readObject());
                                String description = input.nextLine();
                                sendMessage(description);

                                // status
                                System.out.println((String) in.readObject());
                                String status = input.nextLine();
                                sendMessage(status);

                                // Confirmation from server
                                System.out.println((String) in.readObject());
                                break;

                            // Displays all reports (sent from server)
                            case "4":
                                System.out.println((String) in.readObject());
                                break;

                            // Assign report
                            case "5":
                                // report ID
                                System.out.println((String) in.readObject());
                                String reportID = input.nextLine();
                                sendMessage(reportID);

                                // new status
                                System.out.println((String) in.readObject());
                                String newStatus = input.nextLine();
                                sendMessage(newStatus);

                                // Success or failure message
                                System.out.println((String) in.readObject());

                                // Prompt for employee id
                                System.out.println((String) in.readObject());
                                String employeeID = input.nextLine();
                                sendMessage(employeeID);

                                // Confirmation from server
                                System.out.println((String) in.readObject());
                                break;

                            // View assigned reports
                            case "6":
                                System.out.println((String) in.readObject());
                                break;

                            // Update password
                            case "7":
                                System.out.println((String) in.readObject());
                                String newPassword = input.nextLine();
                                sendMessage(newPassword);

                                // Confirmation from server
                                System.out.println((String) in.readObject());
                                break;

                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                        // Continue or exit
                        System.out.println((String) in.readObject());
                        userChoice = input.nextLine();
                        sendMessage(userChoice);

                    } while (!userChoice.equalsIgnoreCase("-1"));
                }

                // Repeat while answer is 1
                message = (String) in.readObject();
                System.out.println(message);
                message = input.nextLine();
                sendMessage(message);
            } while (message.equalsIgnoreCase("1"));

            // Handle unknown host error
        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
            // Handle input/output exceptions
        } catch (IOException ioException) {
            ioException.printStackTrace();
            // Handle error if class of received object is unknown
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 4: Closing connection
            try {
                in.close();
                out.close();
                requestSocket.close();
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

    // Main method
    public static void main(String args[]) {
        Requester client = new Requester();
        client.run();
    }
}