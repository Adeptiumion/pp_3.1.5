package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private static final Logger adminLogger = Logger.getLogger(AdminController.class.getSimpleName());
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(RoleService roleService, UserService userService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> delete(@RequestParam("id") String id) {
        adminLogger.warning("Start destroy user and roles with him id!");
        userService.delete(Integer.parseInt(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/create_user")
    public ResponseEntity<HttpStatus> create(@RequestBody User user) {
        userService.create(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PatchMapping("/update")
    public ResponseEntity<HttpStatus> update(@RequestBody User user) {
        userService.update(user.getId(), user);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/index")
    public ResponseEntity<List<User>> index() {
        List<User> users = userService.readAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> existingRoles() {
        return new ResponseEntity<>(roleService.readAll(), HttpStatus.OK);
    }

    @GetMapping("/current_user")
    public ResponseEntity<User> currentUser(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
