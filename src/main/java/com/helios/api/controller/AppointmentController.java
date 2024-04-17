package com.helios.api.controller;

import com.helios.api.dto.AppointmentDto;
import com.helios.api.dto.NewJobSeekerRequestDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/job-seeker/{jobSeekerId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> loadAppointmentsByJobSeeker(@PathVariable Long jobSeekerId) {
        ResponseDto responseDto = appointmentService.loadAppointmentsByJobSeeker2(jobSeekerId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> loadAllAppointments(
            @RequestParam(name = "startDate", defaultValue = "0") String startDate,
            @RequestParam(name = "endDate", defaultValue = "0") String endDate
    ) {
        ResponseDto responseDto = appointmentService.loadAllAppointments(startDate, endDate);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> createConsultant(@RequestBody AppointmentDto appointmentDto) {
        ResponseDto responseDto = appointmentService.createAppointment(appointmentDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/consultant/{consultantId}/status/{status}")
    @PreAuthorize("hasAnyAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<ResponseDto> loadNewAppointmentsByConsultantId(@PathVariable Long consultantId, @PathVariable int status) {
        ResponseDto responseDto = appointmentService.loadAppointmentsByConsultantId(consultantId, status);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/change-status/{appointmentId}")
    @PreAuthorize("hasAnyAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<ResponseDto> changeStatusOfAppointment(
            @PathVariable Long appointmentId,
            @RequestParam(name = "status") int status,
            @RequestParam(name = "isAccepted") boolean isAccepted) {
        ResponseDto responseDto = appointmentService.changeAppointmentStatus(appointmentId, status, isAccepted);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @DeleteMapping("/{appointmentId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<ResponseDto> deleteAppointment(@PathVariable Long appointmentId) {
        ResponseDto responseDto = appointmentService.removeAppointment(appointmentId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @PostMapping("/new/job-seeker")
    @PreAuthorize("hasAnyAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<ResponseDto> createAnAppointmentForNewJobSeeker(@RequestBody NewJobSeekerRequestDto newJobSeekerRequestDto) {
        ResponseDto responseDto = appointmentService.createAppointmentByReceptionist(newJobSeekerRequestDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
