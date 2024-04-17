package com.helios.api.controller;

import com.helios.api.dto.ResponseDto;
import com.helios.api.service.JobTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/job-type")
public class JobTypeController {
    @Autowired
    private JobTypeService jobTypeService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSULTANT', 'ROLE_USER')")
    public ResponseEntity<ResponseDto> findAllConsultants() {
        ResponseDto responseDto = jobTypeService.fetchJobTypes();
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
