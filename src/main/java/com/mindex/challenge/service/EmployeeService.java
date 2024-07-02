package com.mindex.challenge.service;

import com.mindex.challenge.data.Employee;

public interface EmployeeService {
    // Create, read, and update methods.
    Employee create(Employee employee);
    Employee read(String id);
    Employee update(Employee employee);
}
