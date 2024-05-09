package ru.kata.spring.boot_security.demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    @PersistenceContext
    private final EntityManager entityManager;
    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, EntityManager entityManager, RoleService roleService) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.roleService = roleService;
    }

    @Transactional
    public void create(User user) {
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
            updatedUser.addRoles(readOne(id).getRoles());
            userServiceLogger.info("delete role buffer is ? -> " + updatedUser.getDeletedRoleBuffer());
            if (updatedUser.getDeletedRoleBuffer() != null)
                updatedUser.getRoles().remove(updatedUser.getDeletedRoleBuffer());
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

    @Transactional
    public void createAll() {
        entityManager.createNativeQuery(
                """ 
                        create table if not exists user
                        (
                            id       int primary key auto_increment,
                            name     varchar(10) not null unique,
                            password varchar(64) not null
                        )
                        """
        ).executeUpdate();
        entityManager.createNativeQuery(
                """
                        create table if not exists role
                        (
                            id int primary key auto_increment,
                            value_of_role varchar(30) not null
                        )"""
        ).executeUpdate();
        entityManager.createNativeQuery(
                """
                         create table if not exists user_role
                         (
                             user_id int references user(id),
                             role_id int references role(id),
                             primary key (user_id, role_id)
                         )
                        """
        ).executeUpdate();

        entityManager.flush();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        userServiceLogger.info("!-start-authentication-!");
        Optional<User> user = userRepository.findByEmail(email);
        userServiceLogger.info("User is empty ? -> " + user.isEmpty());
        if (user.isEmpty())
            throw new UsernameNotFoundException("404");
        return user.get();
    }
}