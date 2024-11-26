public class User {
    // User attributes
    private String username, password, email, department, role;
    private int employeeID;

    // Constructor
    public User(String username, String password, String email, String department, String role, int employeeID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.department = department;
        this.role = role;
        this.employeeID = employeeID;
    }

    // Getters and setters
    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Method to print user information
    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + ", email=" + email + ", department=" + department;
    }
}
