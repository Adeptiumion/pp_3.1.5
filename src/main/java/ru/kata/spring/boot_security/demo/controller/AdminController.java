package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger adminLogger = Logger.getLogger(AdminController.class.getSimpleName());
    private final UserService userService;

    @Autowired
    public AdminController(RoleService roleService, UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create_user")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "admin/create_new_user";
    }

    @PostMapping()
    public String create(@RequestParam("role") String role, @ModelAttribute("user") User user) {
        userService.create(role, user);
        return "redirect:/admin"; // Редиректим на главную.
    }

    @GetMapping("/user_by_id")
    public String userPage(@RequestParam("id") String id, Model model) {
        model.addAttribute("user_with_id", userService.readOne(Integer.parseInt(id)));
        return "admin/update_user";
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", userService.readAll());
        return "admin/index";
    }

    @PostMapping("/update")
    public String update
            (
                    @RequestParam("id") String id,
                    @ModelAttribute("user_with_id") User user
            )
    {
        userService.updateFields(Integer.parseInt(id), user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") String id) {
        adminLogger.warning("Start destroy user and roles with him id!");
        userService.delete(Integer.parseInt(id));
        return "redirect:/admin";
    }

    @GetMapping("/update_roles_page")
    public String updatePage(@RequestParam("id") String id, Model model) {
        int ownerId = Integer.parseInt(id); // Вынесу в отдельную переменную пропарсенный в целочисленный вид id-шник.
        User user = userService.readOne(ownerId);
        /* Сконверчу к листу для удобного сравнения. */
        List<String> rolesOfUser = Role.getRolesByStrings(user.getRoles());
        List<String> container = Role.getRolesByStrings(Role.getFullRolesContainer());
        /*  Маркер для определения необходимости отображения кнопки-селектора. */
        boolean marker = rolesOfUser.containsAll(container);
        model.addAttribute("current_user", user); // Передаю юзера в представление его обновления.
        model.addAttribute("marker", marker);
        model.addAttribute("roles_by_strings", rolesOfUser);
        return "admin/update_roles_of_user";
    }

    @PostMapping("/update_roles")
    public String updateRoles(@RequestParam("id") String id, @RequestParam("role") String futureRole) {
        userService.updateRoles(Integer.parseInt(id), futureRole);
        return "redirect:/admin/update_roles_page?id=" + id;
    }

    @PostMapping("/delete_role")
    public String deleteRoleOfUser(@RequestParam("role_id") String roleId, @RequestParam("owner_id") String ownerId) {
        userService.takeAwayRoleOfUser(roleId, ownerId);
        return "redirect:/admin/update_roles_page?id=" + ownerId;
    }


}
