package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserDetailsService, UserService {
    private static final Logger userServiceLogger = Logger.getLogger(UserServiceImpl.class.getSimpleName());
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void create(User user) {
        if (user.getRoles().isEmpty())
            user.addRole(roleRepository.getByValueOfRole("USER"));

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    public User readOne(int id) {
        return userRepository.getReferenceById(id);
    }

    @Transactional
    @Override
    public void update(int id, User updatedUser) {
        if (updatedUser.getRoles().isEmpty())
            updatedUser.setRoles(readOne(updatedUser.getId()).getRoles());

        String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
        updatedUser.setPassword(encodedPassword);

        userServiceLogger.info("User is detected ? -> " + userIsDetected(id));
        if (userIsDetected(id)) {
            updatedUser.setId(id);
            userRepository.save(updatedUser);
        }
    }

    @Transactional
    @Override
    public void delete(int id) {
        User user = userRepository.getReferenceById(id); // Получу юзверя для передачи в метод удаление роли по владельцу.
        userRepository.deleteById(user.getId());
        userServiceLogger.info("User with id " + id + " was deleted!");

    }

    @Override
    public User getById(int id) {
        return userRepository.getReferenceById(id);
    }

    @Override
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