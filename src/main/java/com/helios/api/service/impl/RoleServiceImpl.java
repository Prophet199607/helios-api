package com.helios.api.service.impl;

import com.helios.api.entity.Role;
import com.helios.api.repository.RoleRepository;
import com.helios.api.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByRoleId(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found!"));
    }

    @Override
    public Role createRole(String name) {
        return roleRepository.save(new Role(name));
    }
}
