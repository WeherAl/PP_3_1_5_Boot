package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email")
    private String email;
    @Column(name = "name")
    private String name;
    @Column(name = "last_name")
    private String last_name;
    @Column(name = "age")
    private int age;

    @Column(name = "username", unique = true, nullable = false)
    @Size(min = 2, message = "Не меньше 5 знаков")
    private String username;

    @Column(name = "password")
    @Size(min = 2, message = "Не меньше 5 знаков")
    private String password;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String email, String name, String last_name) {
        this.email = email;
        this.name = name;
        this.last_name = last_name;
    }


    //getters

    public long getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {return email;}

    public String getName() {
        return name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getLast_name() {
        return last_name;
    }



    public void setAge(int age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role roleAdmin) {
        this.roles.add(roleAdmin);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return roles;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email) && Objects.equals(name, user.name) && Objects.equals(last_name, user.last_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, last_name);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", age=" + age +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}

