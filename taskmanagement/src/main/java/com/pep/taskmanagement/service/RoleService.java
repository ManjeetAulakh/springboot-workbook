package com.pep.taskmanagement.service;

import com.pep.taskmanagement.models.Role;

public interface RoleService {

    Role getRoleByName(String name);
    Role createRole(String name); 

}
