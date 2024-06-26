package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.Convertable;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private static final Logger adminLogger = Logger.getLogger(AdminController.class.getSimpleName());
    private final UserService userService;
    private final RoleService roleService;
    private final Convertable mapper;

    @Autowired
    public AdminController(RoleService roleService, UserService userService, Convertable mapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.mapper = mapper;
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
    public ResponseEntity<HttpStatus> update(@RequestBody UserDTO userDTO) {
        adminLogger.info("start update user endpoint logic!\n");
        adminLogger.info("update(user): " + userDTO.toString());
        adminLogger.info("update(user.getRoles()): " + userDTO.getRoles());
        userService.update(userDTO.getId(), mapper.convertToUser(userDTO));
        adminLogger.info("end update user endpoint logic!\n");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/index")
    public ResponseEntity<List<User>> showAll() {
        List<User> users = userService.readAll();
        adminLogger.info("index:  " + users);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getExistingRoles() {
        return new ResponseEntity<>(roleService.readAll(), HttpStatus.OK);
    }

    @GetMapping("/current_user")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/get_user_by_id")
    public ResponseEntity<User> getUserById(@RequestParam int id) {
        adminLogger.info("Id is ->>> " + id);
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

}
