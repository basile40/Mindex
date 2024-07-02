package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("unused")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String compensationUrl;
    private String reportingStructureUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        compensationUrl = "http://localhost:" + port + "/employee/{id}/compensation";
        reportingStructureUrl = "http://localhost:" + port + "/employee/{id}/reportingStructure";
    }

    @SuppressWarnings("null")
    @Test
    public void testCreateReadUpdateEmployee() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("Ringo");
        testEmployee.setLastName("Star");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer V");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);

        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);

        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    @Test
    public void testCreateReadCompensation() {
        // Create a test employee
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Lennon");
        testEmployee.setDepartment("Development Manager");
        testEmployee.setPosition("Developer");

        // Create the test employee via POST request
        ResponseEntity<Employee> createEmployeeResponse = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class);
        assertEquals(HttpStatus.OK, createEmployeeResponse.getStatusCode());
        Employee createdEmployee = createEmployeeResponse.getBody();
        assertNotNull(createdEmployee);
        assertNotNull(createdEmployee.getEmployeeId());

        // Prepare a test compensation object
        Compensation testCompensation = new Compensation();
        testCompensation.setEmployeeId(createdEmployee.getEmployeeId());
        testCompensation.setSalary(75000);
        testCompensation.setEffectiveDate(LocalDate.of(2023, 1, 1));

        // Create the compensation record via POST request
        ResponseEntity<Compensation> createCompensationResponse = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class, createdEmployee.getEmployeeId());
        assertEquals(HttpStatus.OK, createCompensationResponse.getStatusCode());
        Compensation createdCompensation = createCompensationResponse.getBody();
        assertNotNull(createdCompensation);
        assertNotNull(createdCompensation.getEmployeeId());
        assertEquals(testCompensation.getEmployeeId(), createdCompensation.getEmployeeId());
        assertEquals(testCompensation.getSalary(), createdCompensation.getSalary(), 0);
        assertEquals(testCompensation.getEffectiveDate(), createdCompensation.getEffectiveDate());

        // Read the compensation record via GET request
        ResponseEntity<Compensation> readCompensationResponse = restTemplate.getForEntity(compensationUrl, Compensation.class, createdEmployee.getEmployeeId());
        assertEquals(HttpStatus.OK, readCompensationResponse.getStatusCode());
        Compensation readCompensation = readCompensationResponse.getBody();
        assertNotNull(readCompensation);
        assertNotNull(readCompensation.getEmployeeId());
        assertEquals(createdCompensation.getEmployeeId(), readCompensation.getEmployeeId());
        assertEquals(createdCompensation.getSalary(), readCompensation.getSalary(), 0);
        assertEquals(createdCompensation.getEffectiveDate(), readCompensation.getEffectiveDate());
    }

    @Test
    public void testReportingStructure() {
        String employeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";

        ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, employeeId).getBody();

        assertNotNull(reportingStructure);
        assertEquals(employeeId, reportingStructure.getEmployee().getEmployeeId());
        assertEquals(4, reportingStructure.getNumberOfReports());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
