package ru.kata.spring.boot_security.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "value_of_role")
    private String valueOfRole;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> owners;

    // Константа сравнение с которой дает понимание нужно ли вообще отображать select-кнопку и кнопку добавления роли.
    @Getter
    private static final Set<Role> fullRolesContainer = Set.of
            (
                    new Role("USER"),
                    new Role("ADMIN")
            );

    public static final Role USER = new Role("USER");
    public static final Role ADMIN = new Role("ADMIN");

    public static List<String> getRolesByStrings(Set<Role> set) {
        return set.stream().map(e -> "ROLE_" + e.getValueOfRole()).toList();
    }

    public void addOwner(User user) {
        if (owners == null)
            owners = new HashSet<>();
        owners.add(user);
    }

    public Role() {

    }

    public Role(String valueOfRole) {
        this.valueOfRole = valueOfRole;
    }

    public void removeOwnerById(int id) {
        owners.removeIf(e -> e.getId() == id);
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + valueOfRole;
    }

    @Override
    public String toString() {
        return getAuthority();
    }

}
