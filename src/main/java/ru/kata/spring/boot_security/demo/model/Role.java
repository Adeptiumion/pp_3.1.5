package ru.kata.spring.boot_security.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import java.util.HashSet;
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

    @JsonIgnore // Чтоб не было рекурсии в Json.
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> owners;

    public void addOwner(User user) {
        if (owners == null) owners = new HashSet<>();
        owners.add(user);
    }

    public Role() {

    }

    public Role(String valueOfRole) {
        this.valueOfRole = valueOfRole;
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
