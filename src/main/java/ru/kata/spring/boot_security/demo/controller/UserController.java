package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.service.UserService;
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
    public String userPage(@RequestParam("id") String id, Model model){
        userControllerLogger.info("Start show default user!");
        model.addAttribute("default_user_with_id", userService.readOne(Integer.parseInt(id)));
        return "user/user";
    }
}
