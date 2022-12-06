package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final RoleServiceImpl roleService;
    private final PasswordEncoder encoder;


    @Autowired
    public UserServiceImp(UserRepository userRepository, RoleServiceImpl roleService, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void saveUser(User user, String[] role) {
        Set<Role> roles = roleService.addRolesToSet(role);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Transactional
    @Override
    public void updateUser(User user, long id, String[] roles) {
        User userToUpdate = getUserById(id);
        userToUpdate.setLast_name(user.getLast_name());
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setAge(user.getAge());
        userToUpdate.setName(user.getName());
        userToUpdate.setEmail(user.getEmail());
        saveUser(userToUpdate, roles);
    }

    @Override
    public User getUserByUserName(String username) {
        return userRepository.findUserByUsername(username).get();
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getListOfUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findUserByUsername(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("Пользователя с таким username не существует");

        List<GrantedAuthority> authorities = getUserAuthority(user.get().getRoles());

        return buildUserForAuthentication(user.get(), authorities);

    }

    @Override
    public List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        for (Role role : userRoles) {
            roles.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new ArrayList<>(roles);
    }

    @Override
    public UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                true, true, true, true, authorities);
    }


}
