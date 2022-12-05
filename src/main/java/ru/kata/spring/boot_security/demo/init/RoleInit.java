package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;

import javax.annotation.PostConstruct;

@Component
public class RoleInit {
    private final RoleServiceImpl roleService;

    @Autowired
    public RoleInit(RoleServiceImpl roleService) {
        this.roleService = roleService;
    }


    @PostConstruct
    private void postConstruct() {
        if (roleService.getAll().size() == 0) {
            save(new Role("ROLE_ADMIN"), new Role("ROLE_USER"));
        }
    }

    public void save(Role roleUser, Role roleAdmin) {
        roleService.save(roleUser);
        roleService.save(roleAdmin);
    }


}
