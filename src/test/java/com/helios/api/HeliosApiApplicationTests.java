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
    void testSingletonBean() {
        assertEquals(patientService1, patientService2);
    }

}
