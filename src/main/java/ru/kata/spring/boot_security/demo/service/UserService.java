package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void create(User user);

    List<User> readAll();

    User readOne(int id);

    void update(int id, User updatedUser);

    void delete(int id);

    User getById(int id);

    boolean userIsDetected(int id);

}
