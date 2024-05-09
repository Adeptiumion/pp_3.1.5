package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;
import java.util.logging.Logger;

@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger userControllerLogger = Logger.getLogger(UserController.class.getSimpleName());

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public String userPage(Model model, Principal principal){
        User currentUser = userService.findByName(principal.getName());
        userControllerLogger.info("Start show default user!");
        model.addAttribute("default_user_with_id", currentUser);
        return "bootstrap/just_user";
    }
}
