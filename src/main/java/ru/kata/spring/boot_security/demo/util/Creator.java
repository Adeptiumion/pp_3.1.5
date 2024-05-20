package ru.kata.spring.boot_security.demo.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Component
public class Creator {
    @Getter
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public Creator(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @Transactional
    public void enrichAndCreateTables() {
        /* USER */
        Role roleUser = new Role("USER");
        User user = new User()
                .setAge(23)
                .setName("user")
                .setLastName("user")
                .setEmail("user@mail.ru")
                .setPassword("user");
        roleUser.addOwner(user);
        user.addRole(roleUser);
        userService.create(user);
        roleService.create(roleUser);

        /* ADMIN */
        Role roleAdmin = new Role("ADMIN");
        User admin = new User()
                .setAge(23)
                .setName("admin")
                .setLastName("admin")
                .setEmail("admin@mail.ru")
                .setPassword("admin");
        roleAdmin.addOwner(admin);
        roleUser.addOwner(admin);
        admin
                .addRole(roleAdmin)
                .addRole(roleUser);
        userService.create(admin);
        roleService.create(roleAdmin);
    }

}
