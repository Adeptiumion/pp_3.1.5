package ru.kata.spring.boot_security.demo.model;


import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "value_of_role")
    private String valueOfRole;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    // Константа сравнение с которой дает понимание нужно ли вообще отображать select-кнопку и кнопку добавления роли.
    private static final Set<Role> fullRolesContainer = Set.of
            (
                    new Role("USER"),
                    new Role("ADMIN")
            );

    public static Set<Role> getFullRolesContainer() {
        return fullRolesContainer;
    }

    public static List<String> getRolesByStrings(Set<Role> set) {
        return set.stream().map(e -> "ROLE_" + e.getValueOfRole()).toList();
    }

    public Role(){}

    public Role (String valueOfRole){
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValueOfRole() {
        return valueOfRole;
    }

    public void setValueOfRole(String valueOfRole) {
        this.valueOfRole = valueOfRole;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
