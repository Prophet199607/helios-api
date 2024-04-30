package com.helios.api.service.impl;

import com.helios.api.entity.Role;
import com.helios.api.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testFindByRoleId_ExistingRoleId() {
        Long roleId = 1L;
        Role expectedRole = new Role();
        expectedRole.setRoleId(roleId);
        expectedRole.setName("Admin");
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(expectedRole));

        Role resultRole = roleService.findByRoleId(roleId);

        assertEquals(expectedRole.getRoleId(), resultRole.getRoleId());
        assertEquals(expectedRole.getName(), resultRole.getName());
    }

    @Test
    void testFindByRoleId_NonExistingRoleId() {
        Long roleId = 2L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> roleService.findByRoleId(roleId));
    }
}