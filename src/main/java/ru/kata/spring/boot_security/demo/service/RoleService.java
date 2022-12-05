package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Set;


public interface RoleService {
    Set<Role> getAll();

    Role findRoleByName(String role);

    Set<Role> getRole(long id);

    void save(Role role);

    Set<Role> addRolesToSet(String[] role);


}
