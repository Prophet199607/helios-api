package com.helios.api.controller;

import com.helios.api.dto.ResponseDto;
import com.helios.api.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/job-type")
public class UserTypeController {
    private final UserTypeService userTypeService;

    public UserTypeController(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSULTANT', 'ROLE_USER')")
    public ResponseEntity<ResponseDto> findAllConsultants() {
        ResponseDto responseDto = userTypeService.fetchUserTypes();
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
