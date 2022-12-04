package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    //вызов панели администраторга
    @GetMapping("/admin")
    public String getAdminPage(Model model) {
        model.addAttribute("allUsers", userService.listAll());
        return "/admin/index";
    }

    //вызов формы создания
    @GetMapping("/admin/create")
    public String addNewUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "/admin/user-info";
    }
    //редактирования
    @GetMapping(value = "/admin/{id}/edit")
    public String addNewUser(ModelMap model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("allRoles", roleRepository.findAll());
        return "/admin/user-info";
    }

    //функция создания/редактирования польщователя
    @PostMapping
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam("allRoles") String[] role) {
        assert role != null;
        if (role.length != 0) {
            Set<Role> rolesSet = new HashSet<>();
            for (String roles : role) {
                rolesSet.add(roleRepository.findByName(roles));
            }
            user.setRoles(rolesSet);
        }
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
