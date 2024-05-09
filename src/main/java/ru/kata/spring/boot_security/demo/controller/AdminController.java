package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;
import java.util.logging.Logger;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger adminLogger = Logger.getLogger(AdminController.class.getSimpleName());
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(RoleService roleService, UserService userService, RoleService roleService1) {
        this.userService = userService;
        this.roleService = roleService1;
    }


    @PostMapping("/create_user")
    public String create(@ModelAttribute("user") User user) {
        userService.create(user);
        return "redirect:/admin"; // Редиректим на главную.
    }

    @GetMapping("/user_by_id")
    public String userPage(@RequestParam("id") String id, Model model) {
        model.addAttribute("user_with_id", userService.readOne(Integer.parseInt(id)));
        return "admin/update_user";
    }

    @PatchMapping("/update")
    public String update
            (
                    @RequestParam("user_id") String id,
                    @ModelAttribute("user_with_id") User user
            ) {
        adminLogger.info("user before ->>> " + userService.readOne(Integer.parseInt(id)));
        adminLogger.info("user after ->>> " + user);
        adminLogger.info("user id ->>> " + id);
        adminLogger.info("delete role buffer ->>> " + user.getDeletedRoleBuffer());
        userService.update(Integer.parseInt(id), user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam("id") String id) {
        adminLogger.warning("Start destroy user and roles with him id!");
        userService.delete(Integer.parseInt(id));
        return "redirect:/admin";
    }

    @GetMapping()
    public String index(Model model, Principal principal) {
        User usedUser = userService.findByName(principal.getName());
        model.addAttribute("loggedInUser", usedUser);
        model.addAttribute("existingRoles", roleService.readAll());
        model.addAttribute("users", userService.readAll());
        model.addAttribute("newUser", new User());
        model.addAttribute("marker", usedUser.haveThisRole(roleService.findByValueOfRole("USER")));
        return "bootstrap/index";
    }

}
