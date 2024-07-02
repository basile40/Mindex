package com.mindex.challenge.controller;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompensationRepository compensationRepository;

    // Endpoint to create new employee.
    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    // Endpoint to read an employee by ID.
    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.read(id);
    }

    // Endpoint to update an employee by ID.
    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    // Endpoint to get the reporting structure of an employee.
    @GetMapping("/employee/{id}/reportingStructure")
    public ReportingStructure getReportingStructure(@PathVariable String id) {
        LOG.debug("Received getReportingStructure request for id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);
        int numberOfReports = calculateNumberOfReports(employee);

        return new ReportingStructure(employee, numberOfReports);
    }

    // Endpoint to create a compensation entry for an employee.
    @PostMapping("/employee/{id}/compensation")
    public Compensation createCompensation(@PathVariable String id, @RequestBody Compensation compensation) {
        LOG.debug("Received createCompensation request for id [{}] and compensation [{}]", id, compensation);
    
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
    
        compensation.setEmployeeId(id);
        Compensation createdCompensation = compensationRepository.save(compensation);
    
        LOG.debug("Created compensation [{}]", createdCompensation);
    
        return createdCompensation;
    }

    // Endpoint to get a compensation entry for an employee
    @GetMapping("/employee/{id}/compensation")
    public Compensation readCompensation(@PathVariable String id) {
        LOG.debug("Received readCompensation request for id [{}]", id);

        Compensation compensation = compensationRepository.findByEmployeeId(id);
        if (compensation == null) {
            throw new RuntimeException("Compensation not found for employeeId: " + id);
        }

        return compensation;
    }

    // Recursive method to calculate the number of direct and indirect reports.
    private int calculateNumberOfReports(Employee employee) {
        if (employee.getDirectReports() == null) {
            return 0;
        }

        int count = 0;
        for (Employee directReport : employee.getDirectReports()) {
            Employee detailedDirectReport = employeeRepository.findByEmployeeId(directReport.getEmployeeId());
            count += 1 + calculateNumberOfReports(detailedDirectReport);
        }

        return count;
    }
}
