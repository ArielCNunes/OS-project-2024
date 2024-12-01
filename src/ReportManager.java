import java.util.concurrent.ConcurrentHashMap;

public class ReportManager {
    // Store all reports
    private final ConcurrentHashMap<String, Report> reports;

    // Constructor
    public ReportManager() {
        this.reports = new ConcurrentHashMap<>();
    }

    // This method creates a new report
    public Report createReport(User currentUser, String reportType, String description) {
        String reportID = currentUser.getEmail() + "-" + System.currentTimeMillis(); // unique id
        String reportDate = java.time.LocalDate.now().toString(); // date it's been created
        int reportCreatorID = currentUser.getEmployeeID(); // id of current user
        int assignedEmployeeID = -1; // ID unassigned

        reports.put(reportID, new Report(reportType, reportDate, description, reportID, reportCreatorID, assignedEmployeeID));
        return reports.get(reportID);
    }

    // This method returns all reports
    public String getAllReports() {
        if (reports.isEmpty()) {
            return "No reports available.";
        }

        // Put all reports in one String
        StringBuilder allReports = new StringBuilder();
        for (Report report : reports.values()) {
            allReports.append(report.toString()).append("\n"); // Assumes Report has a meaningful toString() implementation
        }

        return allReports.toString();
    }

    // This method updates the status of a report
    public boolean updateReportStatus(String reportID, String newStatus) {
        // Get report
        Report report = reports.get(reportID);

        // Update it only o fit exists
        if (report != null) {
            report.setStatus(newStatus);
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
