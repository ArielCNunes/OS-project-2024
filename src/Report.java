public class Report {
    // Attributes
    private String reportType, reportDate, status, reportID, description;
    private int reportCreatorID, assignedEmployeeID;

    // Constructor
    public Report(String reportType, String reportDate, String description, String reportID, int reportCreatorID, int assignedEmployeeID, String status) {
        this.reportType = reportType;
        this.reportDate = reportDate;
        this.reportID = reportID;
        this.reportCreatorID = reportCreatorID;
        this.assignedEmployeeID = assignedEmployeeID;
        this.description = description;
        this.status = status;
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

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Method to print a report
    @Override
    public String toString() {
        return reportID + "," +
                reportType + "," +
                reportDate + "," +
                description + "," +
                reportCreatorID + "," +
                assignedEmployeeID + "," +
                status;
    }
}
