package com.helios.api.service;

import com.helios.api.entity.Role;

public interface RoleService {

    Role findByRoleId(Long roleId);
    Role createRole(String name);
}
