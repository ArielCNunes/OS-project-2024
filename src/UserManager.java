import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
    // Store all users
    private final ConcurrentHashMap<String, User> users;

    public UserManager() {
        users = new ConcurrentHashMap<>();
    }
    // This method authenticates log information
    public boolean authenticate(String username, String password) {
        return true;
    }

    // This method returns a user
    public User getUserByEmail(String email) {
        return null;
    }

    // This method changes the password
    public boolean updatePassword(String email, String newPassword) {
        return false;
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
