package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompensationRepositoryTest {

    @Autowired
    private CompensationRepository compensationRepository;

    @Test
    public void testCreateReadCompensation() {
        Compensation testCompensation = new Compensation();
        testCompensation.setEmployeeId("62c1084e-6e34-4630-93fd-9153afb65309");
        testCompensation.setSalary(75000);
        testCompensation.setEffectiveDate(LocalDate.of(2023, 1, 1));

        // Create Compensation
        Compensation createdCompensation = compensationRepository.insert(testCompensation);
        assertNotNull(createdCompensation.getId());
        assertEquals(testCompensation.getEmployeeId(), createdCompensation.getEmployeeId());
        assertEquals(testCompensation.getSalary(), createdCompensation.getSalary(), 0);
        assertEquals(testCompensation.getEffectiveDate(), createdCompensation.getEffectiveDate());

        // Read Compensation
        Compensation readCompensation = compensationRepository.findByEmployeeId("62c1084e-6e34-4630-93fd-9153afb65309");
        assertEquals(createdCompensation.getId(), readCompensation.getId());
        assertEquals(createdCompensation.getEmployeeId(), readCompensation.getEmployeeId());
        assertEquals(createdCompensation.getSalary(), readCompensation.getSalary(), 0);
        assertEquals(createdCompensation.getEffectiveDate(), readCompensation.getEffectiveDate());
    }
}

