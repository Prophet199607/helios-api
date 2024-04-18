package com.helios.api;

import com.helios.api.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class HeliosApiApplicationTests {

    @Autowired
    private PatientService patientService1;

    @Autowired
    private PatientService patientService2;

    @Test
    void contextLoads() {
    }

    @Test
    void testSingletonBean() {
        int expected = 10;
//        assertEquals(patientService1, patientService2);
        assertEquals(100, expected);
    }

}
