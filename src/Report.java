import java.util.LinkedList;

public class Report {
    // Attributes
    private String reportType, reportDate, status;
    private int reportID, reportCreatorID, assignedEmployeeID;
    private LinkedList<Report> reports;

    // Constructor
    public Report() {
        // Linked list to hold all reports
        reports = new LinkedList<>();
    }

    // Getters and setters
    public String getReportType() {
        return reportType;
    }
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportDate() {
        return reportDate;
    }
    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public int getReportID() {
        return reportID;
    }
    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public int getReportCreatorID() {
        return reportCreatorID;
    }
    public void setReportCreatorID(int reportCreatorID) {
        this.reportCreatorID = reportCreatorID;
    }

    public int getAssignedEmployeeID() {
        return assignedEmployeeID;
    }
    public void setAssignedEmployeeID(int assignedEmployeeID) {
        this.assignedEmployeeID = assignedEmployeeID;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
