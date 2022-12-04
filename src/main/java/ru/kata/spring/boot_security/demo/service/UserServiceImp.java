package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> listAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void updateUser(User updatedUser) {
        if (userRepository.findById(updatedUser.getId()).isEmpty()){
            User user = new User();
            user.setRoles(updatedUser.getRoles());
            user.setAge(updatedUser.getAge());
            user.setEmail(updatedUser.getEmail());
            user.setName(updatedUser.getName());
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            user.setLast_name(updatedUser.getLast_name());
            userRepository.save(user);
        }else {
            User user = userRepository.findById(updatedUser.getId()).get();

            System.out.println("Пользователь найден");
            System.out.println("ЕГО ID " + user.getId());
            userRepository.save(user);
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("Пользователя с таким username не существует");

        List<GrantedAuthority> authorities = getUserAuthority(user.get().getRoles());

        return buildUserForAuthentication(user.get(), authorities);

    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        for (Role role : userRoles) {
            roles.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new ArrayList<>(roles);
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                true, true, true, true, authorities);
    }


    //создаем пользьователей для тестов
    @Transactional
    public void testCreateUser(String email, String username, String password, String name, String lastName, int age, String role) {
        Role roleAdmin = roleRepository.findByName(role);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
        user.setLast_name(lastName);
        user.setAge(age);
        user.setName(name);
        user.addRole(roleAdmin);
        userRepository.save(user);
    }

    //добавляем роль пользователю для теста
    @Transactional
    public void testAddRoleToNewUser(String username, String role) {
        Role roleAdmin = roleRepository.findByName(role);
        User user = userRepository.findByUsername(username).get();
        user.addRole(roleAdmin);
        userRepository.save(user);
    }

}
