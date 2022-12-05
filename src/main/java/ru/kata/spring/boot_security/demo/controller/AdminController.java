package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Controller
public class AdminController {
    private final UserService userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public AdminController(UserService userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
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
        userService.saveUser(user, role);
        return "redirect:/admin";
    }


    //функция удаления пользователя
    @DeleteMapping("/admin/{id}/delete")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}
