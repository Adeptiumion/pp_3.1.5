package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;
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

    public void enrichAndCreateTables() {
        userService.createAll();
        /* USER */
        Role roleUser = Role.USER;
        User user = User.builder()
                .name("user")
                .password("user")
                .build();
        roleUser.addOwner(user);
        user.addRole(roleUser);
        userService.nativeCreate(user);
        roleService.create(roleUser);

        /* ADMIN */
        Role roleAdmin = Role.ADMIN;
        User admin = User.builder()
                .name("admin")
                .password("admin")
                .build();
        roleAdmin.addOwner(admin);
        admin.addRole(roleAdmin);
        userService.nativeCreate(admin);
        roleService.create(roleAdmin);
    }

}
