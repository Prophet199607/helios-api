package com.helios.api.controller;

import com.helios.api.dto.ResponseDto;
import com.helios.api.service.StaffMemberService;
import com.helios.api.service.PatientService;
import com.helios.api.util.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    private final StaffMemberService staffMemberService;

    private final PatientService patientService;

    public DashboardController(StaffMemberService staffMemberService, PatientService patientService) {
        this.staffMemberService = staffMemberService;
        this.patientService = patientService;
    }


    @GetMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> getFiguresForAdmin() {
        Long totalStaffMembers = staffMemberService.getCountOfRecords();
        Long totalJobSeekers = patientService.getCountOfRecords();

        Map<String, Long> hashMap = new HashMap<>();
        hashMap.put("totalStaffMembers", totalStaffMembers);
        hashMap.put("totalJobSeekers", totalJobSeekers);


        ResponseDto responseDto = new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success!",
                hashMap
        );

        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/consultant/{consultantId}")
    @PreAuthorize("hasAnyAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<ResponseDto> getFiguresForConsultant(@PathVariable Long consultantId) {

        Map<String, Long> hashMap = new HashMap<>();

        ResponseDto responseDto = new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success!",
                hashMap
        );

        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
