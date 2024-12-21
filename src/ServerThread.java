import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {
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
            out.flush();

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

                // Register
                if (message.equalsIgnoreCase("1")) {
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

                    // Parse ID to an int
                    try {
                        int id = Integer.parseInt(idInput);

                        // Add the user if they aren't already added
                        if (userManager.registerNewUser(id, username, email, password, department, role)) {
                            sendMessage("Registration successful! You can now log in.");
                        } else {
                            sendMessage("Registration unsuccessful. Please try again.");
                        }
                    } catch (NumberFormatException e) {
                        sendMessage("Invalid ID. Please enter a valid number.");
                    }
                    // Log in
                } else if (message.equalsIgnoreCase("2")) {
                    sendMessage("Email> ");
                    String email = (String) in.readObject();

                    sendMessage("Password> ");
                    String password = (String) in.readObject();

                    // Check credentials
                    if (userManager.authenticate(email, password)) {
                        sendMessage("Logged in successfully");

                        // Create an instance of the logged-in user
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

            // Catch IOException for communication errors
        } catch (IOException e) {
            e.printStackTrace();
            // Catch exception if received object is of unknown type
        } catch (ClassNotFoundException e) {
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

    // Options 3 - 7 (menu for user logged in)
    private void loggedInMenu(User currentUser) throws IOException, ClassNotFoundException {
        String message;
        do {
            sendMessage("3. Create Report" +
                    "\n4. Retrieve All Reports" +
                    "\n5. Update & assign a report to an employee" +
                    "\n6. View all reports assigned to you" +
                    "\n7. Change Password");
            message = (String) in.readObject();

            switch (message) {
                case "3":
                    createReport(currentUser);
                    break;
                case "4":
                    viewAllReports();
                    break;
                case "5":
                    updateAndAssignReport();
                    break;
                case "6":
                    viewAssignedReports(currentUser);
                    break;
                case "7":
                    changePassword(currentUser);
                    break;
                default:
                    sendMessage("Invalid option. Please try again.");
            }

            sendMessage("Enter 1 to return to the menu, or any other key to log out.");
            message = (String) in.readObject();
        } while (message.equals("1"));
    }

    // Create a report
    private void createReport(User currentUser) throws IOException, ClassNotFoundException {
        sendMessage("Enter report type (Accident or Risk):");
        String reportType = (String) in.readObject();

        sendMessage("Enter a description:");
        String description = (String) in.readObject();

        sendMessage("Enter the status:");
        String status = (String) in.readObject();

        Report newReport = reportManager.createReport(currentUser, reportType, description, status);
        sendMessage("Report created successfully! Report ID: " + newReport.getReportID());
    }

    // View all reports
    private void viewAllReports() throws IOException {
        String reports = reportManager.getAllReports();
        sendMessage(reports);
    }

    // Update & report
    private void updateAndAssignReport() throws IOException, ClassNotFoundException {
        // Update
        sendMessage("Enter the report ID to update:");
        String reportID = (String) in.readObject();

        sendMessage("Enter the new status (Open/Assigned/Closed):");
        String newStatus = (String) in.readObject();

        // Confirmation message
        boolean updated = reportManager.updateReportStatus(reportID, newStatus);
        if (updated) {
            sendMessage("Report updated successfully!");
        } else {
            sendMessage("Failed to update report. Report ID not found.");
        }

        // Assign a report to a user
        sendMessage("Enter employee ID to assign a report: ");
        String employeeIDToAssignReport = (String) in.readObject();
        boolean assigned = reportManager.assignReportToEmployee(reportID, employeeIDToAssignReport);

        if (assigned) {
            sendMessage("Report assigned successfully!");
        } else {
            sendMessage("Failed to assign report. Report ID not found.");
        }
    }

    // View assigned report
    private void viewAssignedReports(User currentUser) {
        // Retrieve any reports assigned to the current user
        String reports = reportManager.getReportsAssignedToUser(currentUser.getEmployeeID());

        // Check if there are any assigned reports
        if (reports.isEmpty()) {
            sendMessage("No reports are currently assigned to you.");
        } else {
            sendMessage("Reports assigned to you:\n" + reports);
        }
    }

    // Change password
    private void changePassword(User currentUser) throws IOException, ClassNotFoundException {
        sendMessage("Enter your new password:");
        String newPassword = (String) in.readObject();

        // Method will return true if password's been updated
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
            // Send message as an object
            out.writeObject(msg);
            // Clear the stream after sending
            out.flush();
            System.out.println("server> " + msg);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
