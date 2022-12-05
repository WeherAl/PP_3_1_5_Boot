package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {

    void saveUser(User user);

    void saveUser(User user, String[] role);

    User getUserById(Long id);

    User getUserByUserName(String username);

    void deleteUserById(Long id);

    List<User> getListOfUsers();

    List<GrantedAuthority> getUserAuthority(Set<Role> userRoles);

    UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities);


}
