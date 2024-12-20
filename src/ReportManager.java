import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

public class ReportManager {
    // Store all reports
    private final ConcurrentHashMap<String, Report> reports;

    // Constructor
    public ReportManager() {
        this.reports = new ConcurrentHashMap<>();
        loadReportsFromFile("src/reports.txt");
    }

    // This method loads the reports from a file
    public void loadReportsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by commas
                String[] parts = line.split(", ");
                if (parts.length == 7) {
                    String reportID = parts[0];
                    String type = parts[1];
                    String date = parts[2];
                    String description = parts[3];
                    int creatorID = Integer.parseInt(parts[4]);
                    int assignedEmployeeID = Integer.parseInt(parts[5]);
                    String status = parts[6];

                    // Create a new Report and add it to the map
                    reports.put(reportID, new Report(type, date, description, reportID, creatorID, assignedEmployeeID, status));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This method creates a new report
    public Report createReport(User currentUser, String reportType, String description, String status) {
        // unique id
        String reportID = currentUser.getEmail() + "-" + System.currentTimeMillis();
        // date it's been created
        String reportDate = java.time.LocalDate.now().toString();
        // id of current user
        int reportCreatorID = currentUser.getEmployeeID();
        // ID unassigned - default value is -1
        int assignedEmployeeID = -1;

        // Add to map and save it to file
        reports.put(reportID, new Report(reportType, reportDate, description, reportID, reportCreatorID, assignedEmployeeID, status));
        saveReportsToFile("src/reports.txt");
        return reports.get(reportID);
    }

    // This method saves reports to a file
    public void saveReportsToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Report report : reports.values()) {
                String line = report.toString();
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Reports saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving reports to file: " + e.getMessage());
        }
    }

    // This method returns all reports
    public String getAllReports() {
        if (reports.isEmpty()) {
            return "No reports available.";
        }

        // Put all reports in one String
        StringBuilder allReports = new StringBuilder();
        for (Report report : reports.values()) {
            allReports.append(report.toString()).append("\n");
        }

        return allReports.toString();
    }

    // This method updates the status of a report
    public boolean updateReportStatus(String reportID, String newStatus) {
        // Get report
        Report report = reports.get(reportID);

        // Update report if it exists
        if (report != null) {
            report.setStatus(newStatus);
            saveReportsToFile("src/reports.txt");
            return true;
        } else {
            return false;
        }
    }

    public boolean assignReportToEmployee(String reportID, String employeeID) {
        Report report = reports.get(reportID);

        if (report == null) {
            return false;
        }

        // Parse id back into an int and assign report to employee
        int id = Integer.parseInt(employeeID);
        report.setAssignedEmployeeID(id);

        saveReportsToFile("src/reports.txt");

        return true;
    }

    // This method returns the report assigned to one user
    public String getReportsAssignedToUser(int employeeID) {
        StringBuilder assignedReports = new StringBuilder();
        for (Report report : reports.values()) {
            if (report.getAssignedEmployeeID() == employeeID) {
                assignedReports.append(report).append("\n");
            }
        }
        return assignedReports.toString();
    }
}
