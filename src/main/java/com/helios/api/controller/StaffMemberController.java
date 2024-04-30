package com.helios.api.controller;

import com.helios.api.dto.StaffMemberDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.entity.StaffMember;
import com.helios.api.service.StaffMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/staffMember")
public class StaffMemberController {
    private final StaffMemberService staffMemberService;

    public StaffMemberController(StaffMemberService staffMemberService) {
        this.staffMemberService = staffMemberService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> findAllStaffMembers() {
        ResponseDto responseDto = staffMemberService.fetchStaffMembers();
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/all/paginate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> findAllStaffMembersWithPaginate(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        ResponseDto responseDto = staffMemberService.fetchAllStaffMembersWithPagination(page - 1, size);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> searchStaffMember(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        ResponseDto staffMembersByName = staffMemberService.findStaffMembersByName(keyword, page - 1, size);
        return new ResponseEntity<>(staffMembersByName, staffMembersByName.getStatus());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> createStaffMember(@RequestBody StaffMember staffMemberDto) {
        ResponseDto responseDto = staffMemberService.createStaffMember(staffMemberDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @DeleteMapping("/{staffMemberId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> deleteInstructor(@PathVariable Long staffMemberId) {
        ResponseDto responseDto = staffMemberService.removeStaffMember(staffMemberId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @PutMapping("/{staffMemberId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> updateInstructor(@RequestBody StaffMemberDto staffMemberDto, @PathVariable Long staffMemberId) {
        staffMemberDto.setStaffMemberId(staffMemberId);
        ResponseDto responseDto = staffMemberService.updateStaffMember(staffMemberDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/find")
    public ResponseEntity<ResponseDto> loadStaffMemberByEmail(@RequestParam(name = "email", defaultValue = "") String email) {
        ResponseDto responseDto = staffMemberService.loadStaffMemberByEmail(email);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/findById/{staffMemberId}")
    public ResponseEntity<ResponseDto> loadStaffMemberByEmail(@PathVariable Long staffMemberId) {
        ResponseDto responseDto = staffMemberService.fetchStaffMemberById(staffMemberId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
