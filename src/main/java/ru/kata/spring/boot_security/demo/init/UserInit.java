package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import javax.annotation.PostConstruct;

@Component
public class UserInit {

    private final RoleServiceImpl roleService;
    private final UserServiceImp userServiceImp;
    private final PasswordEncoder encoder;

    @Autowired
    public UserInit(RoleServiceImpl roleService, UserServiceImp userServiceImp, PasswordEncoder encoder) {
        this.roleService = roleService;
        this.userServiceImp = userServiceImp;
        this.encoder = encoder;
    }
    

    @PostConstruct
    private void postConstruct() {
        if (userServiceImp.getListOfUsers().size() == 0) {
            testCreateUser("avc@", "admin", "admin", "Sasha", "Zurisk", 24, "ROLE_ADMIN");
            testCreateUser("bvc@", "user", "user", "Pasha", "Purisk", 34, "ROLE_USER");
            testCreateUser("bvc@", "userAdmin", "userAdmin", "Tolya", "Potter", 12, "ROLE_USER");
            testAddRoleToNewUser("userAdmin", "ROLE_ADMIN");
        }
    }

    //создаем пользьователей для тестов
    public void testCreateUser(String email, String username, String password, String name, String lastName, int age, String role) {
        Role roleAdmin = roleService.findRoleByName(role);
        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setUsername(username);
        user.setLast_name(lastName);
        user.setAge(age);
        user.setName(name);
        user.addRole(roleAdmin);
        userServiceImp.saveUser(user);
    }

    //добавляем роль пользователю для теста
    public void testAddRoleToNewUser(String username, String role) {
        Role roleAdmin = roleService.findRoleByName(role);
        User user = userServiceImp.getUserByUserName(username);
        user.addRole(roleAdmin);
        userServiceImp.saveUser(user);
    }

}
