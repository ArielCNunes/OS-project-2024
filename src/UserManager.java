import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

public class UserManager {
    // Map to store users
    private final ConcurrentHashMap<String, User> users;

    //  Constructor
    public UserManager() {
        users = new ConcurrentHashMap<>();
        loadUsersFromFile("src/employees.txt");
    }

    // This method loads the map with the users from a file
    public void loadUsersFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            System.out.println("Loading employees.txt");
            while ((line = reader.readLine()) != null) {
                // Split the line by commas
                String[] parts = line.split(", ");
                if (parts.length == 6) {
                    int employeeID = Integer.parseInt(parts[0]);
                    String username = parts[1];
                    String email = parts[2];
                    String password = parts[3];
                    String department = parts[4];
                    String role = parts[5];

                    // Create a new User and add it to the map
                    users.put(email, new User(employeeID, username, email, password, department, role));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This method authenticates log in information
    public boolean authenticate(String email, String password) {
        // Check if email exists
        if (!users.containsKey(email)) {
            return false;
        }

        // Now return either true or false
        return users.get(email).getPassword().equals(password);
    }

    // This method returns a user
    public User getUserByEmail(String email) {
        return users.get(email);
    }

    // This method updates the password
    public boolean updatePassword(String email, String newPassword) {
        // Check if user exists
        if (!users.containsKey(email)) {
            return false;
        }

        // If so, update password and save to file
        users.get(email).setPassword(newPassword);
        saveUsersToFile("src/employees.txt");
        return true;
    }

    // This email registers the user (if the user is not already registered)
    public boolean registerNewUser(int employeeID, String username, String email, String password, String department, String role) {
        if (users.containsKey(email)) {
            return false; // Email already registered
        }

        // Loop through users to make sure id is unique
        for (User user : users.values()) {
            if (user.getEmployeeID() == employeeID) {
                return false; // ID already exists
            }
        }

        // Add new user and save it file
        users.put(email, new User(employeeID, username, email, password, department, role));
        saveUsersToFile("src/employees.txt");
        return true;
    }

    public void saveUsersToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Loop through users, store the toString in a line and save it to a file
            for (User user : users.values()) {
                String line = user.toString();
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Users saved to file successfully.");
        } catch (IOException e) {
            System.err.println("Error saving users to file: " + e.getMessage());
        }
    }
}
