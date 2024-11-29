import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
    // Store all users
    private final ConcurrentHashMap<String, User> users;

    public UserManager() {
        users = new ConcurrentHashMap<>();
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
