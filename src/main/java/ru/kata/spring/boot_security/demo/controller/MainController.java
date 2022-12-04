package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;


@Controller
public class MainController {

    private final UserServiceImp userServiceImp;
    private final UserRepository userRepository;

    @Autowired
    public MainController(UserServiceImp userServiceImp, UserRepository userRepository) {
        this.userServiceImp = userServiceImp;
        this.userRepository = userRepository;
    }
    @GetMapping("/")
    public String mainPage(Model model) {
        if (userServiceImp.listAll().size() == 0) {
            userServiceImp.testCreateUser("avc@", "admin", "admin", "Sasha", "Zurisk", 24, "ROLE_ADMIN");
            userServiceImp.testCreateUser("bvc@", "user", "user", "Pasha", "Purisk", 34, "ROLE_USER");
            userServiceImp.testCreateUser("bvc@", "userAdmin", "userAdmin", "Tolya", "Potter", 12, "ROLE_USER");
            userServiceImp.testAddRoleToNewUser("userAdmin", "ROLE_ADMIN");
        }
        model.addAttribute("allUsers", userServiceImp.listAll());
        return "main";
    }


}
