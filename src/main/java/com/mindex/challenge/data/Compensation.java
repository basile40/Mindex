package com.mindex.challenge.data;

import java.time.LocalDate;

/*
 * Class representing the compensation details of an employee.
 * Contains the effective date and the salary of an employee.
 */
public class Compensation {
    private String id;
    private String employeeId;
    private float salary;
    private LocalDate effectiveDate;

    // Setters and getters.
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
