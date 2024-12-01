import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

public class UserManager {
    // Store all users
    private final ConcurrentHashMap<String, User> users;

    public UserManager() {
        users = new ConcurrentHashMap<>();
        loadUsersFromFile("employees.txt");
    }

    public void loadUsersFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            System.out.println("Loading employees.txt");
            while ((line = reader.readLine()) != null) {
                // Split the line by commas
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    int employeeID = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String email = parts[2];
                    String password = parts[3];
                    String department = parts[4];
                    String role = parts[5];

                    // Create a new User and add it to the map
                    users.put(email, new User(name, password, email, department, role, employeeID));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
    public boolean registerNewUser(String username, String password, String email, String department, String role, int employeeID) {
        if (users.containsKey(email)) {
            return false; // Email already registered
        }
        users.put(email, new User(username, password, email, department, role, employeeID)); // Add new user
        return true;
    }
}
