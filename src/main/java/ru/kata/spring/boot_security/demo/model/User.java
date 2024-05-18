package ru.kata.spring.boot_security.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(name = "last_name") // Да, именно здесь юзаю @Column.
    private String lastName;
    private int age;
    private String email;
    private String password;
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;


    public User(String name, String password, Set<Role> roles) {
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User() {

    }

    public User addRole(Role role) {
        if (roles == null) roles = new HashSet<>();
        roles.add(role);
        return this;
    }

    public boolean haveThisRole(Role role) {
        return roles.contains(role);
    }

    public boolean haveThisSetOfRoles(Set<Role> roleSet) {
        return roles.containsAll(roleSet);
    }

    @Override
    public String toString() {
        return "\nid is --- " + getId() + "\nname is --- " + getName() + "\nroles is --- " + getRoles() + "\n";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.getName();
    }

    /**
     * Ниже везде ставлю 'true' для заглушки.
     */

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
}
