package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Set<Role> getAll() {
        return new HashSet<>(roleRepository.findAll());
    }

    @Override
    public Role findRoleByName(String role) {
        return roleRepository.findRoleByName(role);
    }

    @Override
    public Set<Role> addRolesToSet(String[] role) {
        Set<Role> rolesSet = new HashSet<>();
        if (role.length != 0) {
            for (String roles : role) {
                rolesSet.add(roleRepository.findRoleByName(roles));
            }
        }
        return rolesSet;
    }

    @Override
    public Set<Role> getRole(long id) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.getById(id));
        return roles;
    }

    @Transactional
    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }
}
