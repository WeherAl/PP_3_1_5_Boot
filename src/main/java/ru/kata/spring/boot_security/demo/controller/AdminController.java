package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.init.RoleInit;
import ru.kata.spring.boot_security.demo.init.UserInit;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Controller
public class AdminController {
    private final UserService userService;
    private final RoleServiceImpl roleService;
    private final RoleInit roleInit;
    private final UserInit userInit;
    private final PasswordEncoder encoder;

    @Autowired
    public AdminController(UserService userService, RoleServiceImpl roleService, RoleInit roleInit, UserInit userInit, PasswordEncoder encoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.roleInit = roleInit;
        this.userInit = userInit;
        this.encoder = encoder;
    }


    @GetMapping("/")
    public String getMainPage(Model model) {
        if (roleService.getAll().size() == 0) {
            roleInit.save(new Role("ROLE_ADMIN"), new Role("ROLE_USER"));
        }
        if (userService.getListOfUsers().size() == 0) {
            userInit.testCreateUser("avc@", "admin", "admin", "Sasha", "Zurisk", 24, "ROLE_ADMIN");
            userInit.testCreateUser("bvc@", "user", "user", "Pasha", "Purisk", 34, "ROLE_USER");
            userInit.testCreateUser("bvc@", "userAdmin", "userAdmin", "Tolya", "Potter", 12, "ROLE_USER");
            userInit.testAddRoleToNewUser("userAdmin", "ROLE_ADMIN");
        }
        model.addAttribute("allUsers", userService.getListOfUsers());

        return "main";
    }

    //вызов панели администраторга
    @GetMapping("/admin")
    public String getAdminPage(Model model) {
        model.addAttribute("allUsers", userService.getListOfUsers());
        return "/admin/index";
    }

    //вызов формы создания
    @GetMapping("/admin/create")
    public String getUserFormForCreate(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAll());
        return "/admin/user-info";
    }

    //редактирования
    @GetMapping(value = "/admin/{id}/edit")
    public String getUserFormForUpdate(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("allRoles", roleService.getAll());
        return "/admin/user-info";
    }

    //функция создания/редактирования пользователя
    @PostMapping("/admin/save")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam("allRoles") String[] role) {
        Set<Role> roles = roleService.addRolesToSet(role);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(roles);
        userService.saveUser(user);
        return "redirect:/admin";
    }


    //функция удаления пользователя
    @DeleteMapping("/admin/{id}/delete")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}
