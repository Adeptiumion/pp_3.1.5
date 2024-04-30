package ru.kata.spring.boot_security.demo.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    @NotEmpty(message = "У вас не может быть пустого имени")
    @Pattern(regexp = "[a-zA-Z а-яА-Я]+", message = "Имя должно содержать только буквы!")
    @Size(min = 2, max = 10, message = "Число символов должно быть от 2 до 10!")
    private String name;

    @Column(name = "password")
    @Size(min = 4, max = 64, message = "Пароль должен содержать минимум 4 символа и  максимум 64!")
    private String password;
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<Role> roles; // 2 раза писать, что юзверь-админ не хочу. Поэтому в сет роли кладу.

    public User() {

    }


    public User(String name, Set<Role> roles) {
        this.name = name;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        if (roles == null)
            roles = new HashSet<>();
        roles.add(role);
    }


    @Override
    public String toString() {
        return "\nid is --- " + getId() + "\nname is --- " + getName() + "\nroles is --- " + getRoles() + "\n";
    }
}
