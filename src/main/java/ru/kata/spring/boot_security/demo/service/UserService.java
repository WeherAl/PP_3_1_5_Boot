package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Component
public interface UserService extends UserDetailsService {

    public void saveUser(User user);
    public User getUserById(Long id);
    public void deleteUserById(Long id);
    public List<User> listAll();
    void updateUser(User user);
}
