package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.logging.Logger;

@Component
public class Creator {
    private final Logger creatorLogger = Logger.getLogger(Creator.class.getSimpleName());
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public Creator(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @Transactional
    public void enrichAndCreateTables() {
        userService.createAll();
        /* USER */
        Role roleUser = Role.USER;
        User user = User.builder()
                .age(23)
                .name("user")
                .lastName("user")
                .email("user@mail.ru")
                .password("user")
                .build();
        roleUser.addOwner(user);
        user.addRole(roleUser);
        userService.create(user);
        roleService.create(roleUser);

        /* ADMIN */
        Role roleAdmin = Role.ADMIN;
        User admin = User.builder()
                .age(23)
                .name("admin")
                .lastName("admin")
                .email("admin@mail.ru")
                .password("admin")
                .build();
        roleAdmin.addOwner(admin);
        roleUser.addOwner(admin);
        admin
                .addRole(roleAdmin)
                .addRole(roleUser);
        userService.create(admin);
        roleService.create(roleAdmin);
    }

}
