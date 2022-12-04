package ru.kata.spring.boot_security.demo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    private final TestEntityManager entityManager;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    @Autowired
    public UserRepositoryTests(TestEntityManager entityManager, UserRepository userRepo, RoleRepository roleRepo) {
        this.entityManager = entityManager;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Test
    public void testCreateUser() {
        Role roleAdmin = roleRepo.findByName("ROLE_USER");

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("ravi2020");
        user.setUsername("Ravi");
        user.setLast_name("Kumar");
        user.setAge(34);
        user.setName("Boo");
        user.addRole(roleAdmin);

        User savedUser = userRepo.save(user);
        User existUser = entityManager.find(User.class, savedUser.getId());
        assertThat(user.getEmail()).isEqualTo(existUser.getEmail());

    }

    @Test
    public void testAddRoleToNewUser() {

        Role roleAdmin = roleRepo.findByName("ROLE_ADMIN");
        User user = userRepo.findByUsername("zurisk").get();
        user.addRole(roleAdmin);
        User savedUser = userRepo.save(user);

        assertThat(savedUser.getRoles().size()).isEqualTo(1);
    }


}