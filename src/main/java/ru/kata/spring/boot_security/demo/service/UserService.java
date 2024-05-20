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
public class UserService implements UserDetailsService {
    private static final Logger userServiceLogger = Logger.getLogger(UserService.class.getSimpleName());
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void create(User user) {
        if (user.getRoles().isEmpty())
            user.addRole(roleRepository.getByValueOfRole("USER"));
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Deprecated
    public List<User> readAll() {
        List<User> users = userRepository.findAll();
        return users;
    }


    @Transactional
    public List<User> readAllWithLoadRoles(){
        List<User> userList = userRepository.findAllWithRoles();
        userServiceLogger.info("readAllWithLoadRoles: " + userList);
        return userRepository.findAllWithRoles();
    }

    public User readOne(int id) {
        return userRepository.getReferenceById(id);
    }

    @Transactional
    public void update(int id, User updatedUser) {
        if (updatedUser.getRoles().isEmpty())
            updatedUser.setRoles(readOne(updatedUser.getId()).getRoles());
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

    public User getById(int id) {
        return userRepository.getReferenceById(id);
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