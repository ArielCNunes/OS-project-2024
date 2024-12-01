import java.util.UUID;
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
        return null;
    }

    // This method updates the status of a report
    public boolean updateReportStatus(String reportID, String newStatus) {
        return false;
    }

    // This method returns the report assigned to one user
    public String getReportsAssignedToUser(String email) {
        return null;
    }
}
