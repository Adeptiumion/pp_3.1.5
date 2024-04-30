package ru.kata.spring.boot_security.demo.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Optional;

@Component
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        Optional<User> foundedUser = userRepository.findByName(user.getName());
        // Валидация гарантии уникальности имени
        if (foundedUser.isPresent() && foundedUser.get().getName().equals(user.getName())) {
            errors.rejectValue
                    (
                            "name",
                            "",
                            "Создать или обновить пользователя с таким именем невозможно. " +
                                    "\nПользователь с таким именем уже зарегистрирован в базе данных!"
                    );
        }
    }
}
