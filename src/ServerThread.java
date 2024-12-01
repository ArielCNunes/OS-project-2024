import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {
    // Fields
    private final Socket socket;
    private final ReportManager reportManager;
    private final UserManager userManager;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // Constructor
    public ServerThread(Socket socket, ReportManager reportManager, UserManager userManager) {
        this.socket = socket;
        this.reportManager = reportManager;
        this.userManager = userManager;
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
            String message;
            do {
                int optionChosen;
                do {
                    // Menu (for users not logged in)
                    sendMessage("1. Register new user" + "\n2. Login to the Health and Safety Reporting System");

                    // Get input and parse it
                    message = (String) in.readObject();
                    optionChosen = Integer.parseInt(message);
                } while ((optionChosen != 1) && (optionChosen != 2));

                // Register & log in
                if (message.equalsIgnoreCase("1")) {
                    // Registration logic
                    sendMessage("Create a username> ");
                    String username = (String) in.readObject();

                    sendMessage("Create an email> ");
                    String email = (String) in.readObject();

                    sendMessage("Create a password> ");
                    String password = (String) in.readObject();

                    sendMessage("Set a department> ");
                    String department = (String) in.readObject();

                    sendMessage("Set a role> ");
                    String role = (String) in.readObject();

                    sendMessage("Enter your employee ID> ");
                    String idInput = (String) in.readObject();

                    try {
                        int id = Integer.parseInt(idInput); // Parse to int
                        if (userManager.registerNewUser(username, email, password, department, role, id)) {
                            sendMessage("Registration successful! You can now log in.");
                        } else {
                            sendMessage("Registration unsuccessful. Please try again.");
                        }
                    } catch (NumberFormatException e) {
                        sendMessage("Invalid ID. Please enter a valid number.");
                    }
                } else if (message.equalsIgnoreCase("2")) {
                    // Validate user
                    sendMessage("Email> ");
                    String email = (String) in.readObject();

                    sendMessage("Password> ");
                    String password = (String) in.readObject();

                    // Check credentials
                    if (userManager.authenticate(email, password)) {
                        User currentUser = userManager.getUserByEmail(email);
                        loggedInMenu(currentUser);
                    } else {
                        sendMessage("Invalid email or password");
                    }
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

    // Options 3 - 7
    private void loggedInMenu(User currentUser) throws IOException, ClassNotFoundException {
        String message;
        do {
            sendMessage("3. Create Report" +
                    "\n4. View All Reports" +
                    "\n5. Update Report" +
                    "\n6. View Assigned Reports" +
                    "\n7. Change Password");
            message = (String) in.readObject();

            int optionChosen;

            // Validate input
            try {
                optionChosen = Integer.parseInt(message);
            } catch (NumberFormatException e) {
                sendMessage("Invalid input. Please enter a number between 3 and 7.");
                continue;
            }

            switch (optionChosen) {
                case 3:
                    createReport(currentUser);
                    break;
                case 4:
                    viewAllReports();
                    break;
                case 5:
                    updateReport();
                    break;
                case 6:
                    viewAssignedReports(currentUser);
                    break;
                case 7:
                    changePassword(currentUser);
                    break;
                default:
                    sendMessage("Invalid option. Please try again.");
            }

            sendMessage("Enter 1 to return to the menu, or any other key to log out.");
            message = (String) in.readObject();
        } while (message.equals("1"));
    }

    // Create report
    private void createReport(User currentUser) throws IOException, ClassNotFoundException {
        sendMessage("Enter report type (Accident or Risk):");
        String reportType = (String) in.readObject();

        sendMessage("Enter a description:");
        String description = (String) in.readObject();

        Report newReport = reportManager.createReport(currentUser, reportType, description);
        sendMessage("Report created successfully! Report ID: " + newReport.getReportID());
    }

    // View all reports
    private void viewAllReports() throws IOException {
        String reports = reportManager.getAllReports();
        sendMessage(reports);
    }

    // Update report
    private void updateReport() throws IOException, ClassNotFoundException {
        sendMessage("Enter the report ID to update:");
        String reportID = (String) in.readObject();

        sendMessage("Enter the new status (Open/Assigned/Closed):");
        String newStatus = (String) in.readObject();

        boolean updated = reportManager.updateReportStatus(reportID, newStatus);
        if (updated) {
            sendMessage("Report updated successfully!");
        } else {
            sendMessage("Failed to update report. Report ID not found.");
        }
    }

    // View assigned report
    private void viewAssignedReports(User currentUser) throws IOException {
        String assignedReports = reportManager.getReportsAssignedToUser(currentUser.getEmail());
        sendMessage(assignedReports);
    }

    // Change password
    private void changePassword(User currentUser) throws IOException, ClassNotFoundException {
        sendMessage("Enter your new password:");
        String newPassword = (String) in.readObject();

        boolean success = userManager.updatePassword(currentUser.getEmail(), newPassword);
        if (success) {
            sendMessage("Password updated successfully!");
        } else {
            sendMessage("Failed to update password.");
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
