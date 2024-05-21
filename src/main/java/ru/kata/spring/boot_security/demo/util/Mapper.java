package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.HashSet;
import java.util.Set;

@Component
public class Mapper implements Convertable {

    private final RoleService roleService;

    @Autowired
    public Mapper(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setAge(userDTO.getAge());
        user.setEmail(userDTO.getEmail());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        Set<Role> roles = new HashSet<>();
        for (RoleDTO idOfRole : userDTO.getRoles())
            roles.add(roleService.getById(idOfRole.getId()));
        user.setRoles(roles);
        return user;
    }
}
