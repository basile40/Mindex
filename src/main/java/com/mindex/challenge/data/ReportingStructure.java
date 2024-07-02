package com.mindex.challenge.data;

/*
 * A class representing the structure of a report of an employee.
 * Contains an employee and the total number of reports.
 */
public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;

    // Constructor
    public ReportingStructure(Employee employee, int numberOfReports) {
        this.employee = employee;
        this.numberOfReports = numberOfReports;
    }

    // Setters and getters.
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
}
