package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.dao.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportingStructureTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void testCalculateNumberOfReports() {
        Employee employee1 = new Employee();
        employee1.setEmployeeId("1");
        Employee employee2 = new Employee();
        employee2.setEmployeeId("2");
        Employee employee3 = new Employee();
        employee3.setEmployeeId("3");

        employee2.setDirectReports(Arrays.asList(employee3));
        employee1.setDirectReports(Arrays.asList(employee2));

        employeeRepository.insert(employee1);
        employeeRepository.insert(employee2);
        employeeRepository.insert(employee3);

        Employee readEmployee1 = employeeRepository.findByEmployeeId("1");
        int numberOfReports = calculateNumberOfReports(readEmployee1);

        ReportingStructure reportingStructure = new ReportingStructure(readEmployee1, numberOfReports);
        assertNotNull(reportingStructure);
        assertEquals(2, reportingStructure.getNumberOfReports());
    }

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