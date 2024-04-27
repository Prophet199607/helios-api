package com.helios.api.controller;

import com.helios.api.dto.PatientDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.service.AppointmentService;
import com.helios.api.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSULTANT')")
    public ResponseEntity<ResponseDto> findAllPatients() {
        ResponseDto responseDto = patientService.fetchPatients();
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/all/paginate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSULTANT')")
    public ResponseEntity<ResponseDto> findPatientsWithPaginate(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        ResponseDto responseDto = patientService.fetchAllPatientsWithPagination(page - 1, size);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping
    public ResponseEntity<ResponseDto> searchPatients(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        ResponseDto consultantsByName = patientService.findPatientsByName(keyword, page - 1, size);
        return new ResponseEntity<>(consultantsByName, consultantsByName.getStatus());
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createPatient(@RequestBody PatientDto patientDto) {
        ResponseDto responseDto = patientService.createPatient(patientDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @DeleteMapping("/{patientId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> deletePatient(@PathVariable Long patientId) {
        ResponseDto responseDto = patientService.removePatient(patientId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<ResponseDto> updateInstructor(@RequestBody PatientDto patientDto, @PathVariable Long patientId) {
        patientDto.setPatientId(patientId);
        ResponseDto responseDto = patientService.updatePatient(patientDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/find")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LABORATORY')")
    public ResponseEntity<ResponseDto> loadAppointmentsByNicAndStatus(
            @RequestParam(name = "nic", defaultValue = "") String nic,
            @RequestParam(name = "status", defaultValue = "1") int status
    ) {
        ResponseDto responseDto = appointmentService.loadAppointmentsByNicAndStatus(nic, status);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/findById/{patientId}")
    public ResponseEntity<ResponseDto> loadPatientByEmail(@PathVariable Long patientId) {
        ResponseDto responseDto = patientService.fetchPatientById(patientId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
