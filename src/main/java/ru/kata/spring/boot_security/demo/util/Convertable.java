package ru.kata.spring.boot_security.demo.util;

import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;

public interface Convertable {
    User convertToUser(UserDTO userDTO);
}
