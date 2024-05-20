import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kata.spring.boot_security.demo.configs.JUnitConfig;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.logging.Logger;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JUnitConfig.class)
@SpringBootTest
public class UserServiceTest {
    private static final Logger userServiceTestLogger = Logger.getLogger(UserServiceTest.class.getSimpleName());
    @Autowired
    private UserService userService;
    @Autowired
    private EntityManager entityManager;

    @Test
    public void resultOfReadAllShouldBeNotEmpty(){
        // action
        List<User> users = userService.readAllWithLoadRoles();
        // log
        userServiceTestLogger.info("resultOfReadAllShouldBeNotEmpty: " + users);
        // assertion
        Assertions.assertFalse(users.isEmpty());
    }



}
