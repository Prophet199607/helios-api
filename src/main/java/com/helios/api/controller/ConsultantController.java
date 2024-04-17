package com.helios.api.controller;

import com.helios.api.dto.ConsultantDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.entity.Consultant;
import com.helios.api.service.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/consultant")
public class ConsultantController {
    @Autowired
    private ConsultantService consultantService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSULTANT')")
    public ResponseEntity<ResponseDto> findAllConsultants() {
        ResponseDto responseDto = consultantService.fetchConsultants();
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/all/paginate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSULTANT')")
    public ResponseEntity<ResponseDto> findAllConsultantsWithPaginate(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        ResponseDto responseDto = consultantService.fetchAllConsultantsWithPagination(page - 1, size);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> searchConsultant(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        ResponseDto consultantsByName = consultantService.findConsultantsByName(keyword, page - 1, size);
        return new ResponseEntity<>(consultantsByName, consultantsByName.getStatus());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> createConsultant(@RequestBody Consultant consultantDto) {
        ResponseDto responseDto = consultantService.createConsultant(consultantDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @DeleteMapping("/{consultantId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> deleteInstructor(@PathVariable Long consultantId) {
        ResponseDto responseDto = consultantService.removeConsultant(consultantId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @PutMapping("/{consultantId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> updateInstructor(@RequestBody ConsultantDto consultantDto, @PathVariable Long consultantId) {
        consultantDto.setConsultantId(consultantId);
        ResponseDto responseDto = consultantService.updateConsultant(consultantDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/find")
    public ResponseEntity<ResponseDto> loadConsultantByEmail(@RequestParam(name = "email", defaultValue = "") String email) {
        ResponseDto responseDto = consultantService.loadConsultantByEmail(email);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/findById/{consultantId}")
    public ResponseEntity<ResponseDto> loadConsultantByEmail(@PathVariable Long consultantId) {
        ResponseDto responseDto = consultantService.fetchConsultantById(consultantId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
