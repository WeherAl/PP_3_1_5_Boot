package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.service.UserService;



@RestController
@RequestMapping("/user/api")
public class UserRestApiController {
    private final UserService userService;

    @Autowired
    public UserRestApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserDTO getUser(Authentication auth) {
        return userService.convertToUserDTO(userService.getUserByUserName(auth.getName()));
    }
}
