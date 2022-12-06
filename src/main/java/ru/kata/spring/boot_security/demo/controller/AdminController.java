package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

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
    public String getAdminPage(Principal principal, Model model) {
        model.addAttribute("allUsers", userService.getListOfUsers());
        model.addAttribute("allRoles", roleService.getAll());
        model.addAttribute("newUser", new User());
        User user = userService.getUserByUserName(principal.getName());
        model.addAttribute("authUser", user);
        return "/admin/admin";
    }

    //функция редактирования пользователя
    @PostMapping("/admin/{id}/edit")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam("allRoles") String[] role,
                             @PathVariable("id") long id) {
        userService.updateUser(user, id, role);
        return "redirect:/admin";
    }

    //функция создания пользователя
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
