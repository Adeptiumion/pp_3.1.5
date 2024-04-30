package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.util.Cleaner;

import java.util.List;
import java.util.logging.Logger;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final Logger userServiceLogger = Logger.getLogger(UserService.class.getSimpleName());
    private final UserRepository userRepository;
    private final Cleaner cleaner; // Для очистки бд-шки прям в приложении.

    @Autowired
    public UserService(UserRepository userRepository, Cleaner cleaner) {
        this.userRepository = userRepository;
        this.cleaner = cleaner;
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
        userServiceLogger.info("Detected ? -> " + userIsDetected(id));
        if (userIsDetected(id)) {
            updatedUser.setId(id);
            userRepository.save(updatedUser);
        }

    }

    @Transactional
    public void delete(int id) {
        if (userIsDetected(id))
            userRepository.deleteById(id);
    }

    public User findByName(String name){
        return userRepository.findByName(name).get();
    }


    @Transactional
    public void truncateAll() {
        cleaner.truncateAll();
    }

    public boolean userIsDetected(int id) {
        return userRepository.findById(id).isPresent();
    }
}