package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final Logger userServiceLogger = Logger.getLogger(UserService.class.getSimpleName());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void create(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public List<User> readAll() {
        return userRepository.findAll();
    }

    public User readOne(int id) {
        return userRepository.getReferenceById(id);
    }

    @Transactional
    public void update(int id, User updatedUser) {
        userServiceLogger.info("User is detected ? -> " + userIsDetected(id));
        if (userIsDetected(id)) {
            System.err.println(updatedUser);
            updatedUser.setId(id);
            userRepository.save(updatedUser);
        }

    }

    @Transactional
    public void delete(int id) {
        User user = userRepository.getReferenceById(id); // Получу юзверя для передачи в метод удаление роли по владельцу.
        userRepository.deleteById(user.getId());
        userServiceLogger.info("User with id " + id + " was deleted!");

    }

    public User findByName(String name) {
        return userRepository.findByName(name).get();
    }

    public boolean userIsDetected(int id) {
        return userRepository.findById(id).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        userServiceLogger.info("!-start-authentication-!");
        Optional<User> user = userRepository.findByEmail(email);
        userServiceLogger.info("User is empty ? -> " + user.isEmpty());
        if (user.isEmpty()) throw new UsernameNotFoundException("404");
        return user.get();
    }
}