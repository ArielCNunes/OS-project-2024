import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

public class UserManager {
    // Store all users
    private final ConcurrentHashMap<String, User> users;

    public UserManager() {
        users = new ConcurrentHashMap<>();
        loadUsersFromFile("/Users/arielnunes/local-college/project-2024/OS-project-2024/src/employees.txt");
    }

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

    public void saveUsersToFile(String filePath) {
        System.out.println("Saving employees to " + filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
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

    // This method authenticates log in information
    public boolean authenticate(String email, String password) {
        // Email
        if (!users.containsKey(email)) {
            return false;
        }

        // Password
        return users.get(email).getPassword().equals(password);
    }

    // This method returns a user
    public User getUserByEmail(String email) {
        return users.get(email);
    }

    // This method updates the password
    public boolean updatePassword(String email, String newPassword) {
        if (!users.containsKey(email)) {
            return false;
        }
        users.get(email).setPassword(newPassword);
        return true;
    }

    // This email registers the user (if the user is not already registered)
    public boolean registerNewUser(int employeeID, String username, String email, String password, String department, String role) {
        if (users.containsKey(email)) {
            return false; // Email already registered
        }

        // Add new user and save it file
        saveUsersToFile("/Users/arielnunes/local-college/project-2024/OS-project-2024/src/employees.txt");
        users.put(email, new User(employeeID, username, email, password, department, role));
        return true;
    }
}
