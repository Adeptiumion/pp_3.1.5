package ru.kata.spring.boot_security.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger adminLogger = Logger.getLogger(AdminController.class.getSimpleName());
    private final RoleService roleService;
    private final UserService userService;
    private final Validator validator;

    @Autowired
    public AdminController(RoleService roleService, UserService userService, @Qualifier("userValidator") Validator validator) {
        this.roleService = roleService;
        this.userService = userService;
        this.validator = validator;
    }


    @GetMapping("/create_user")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "admin/create_new_user";
    }

    @PostMapping()
    public String create(@RequestParam("role") String role, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        validator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) return "admin/create_new_user";

        Role insertableRole = new Role(role); // Передаваемая роль.
        insertableRole.setOwner(user); // "Сетаю" владельца роли.
        user.addRole(insertableRole); // Передаю роль челу.
        userService.create(user); // Сохраняю чела.
        roleService.create(insertableRole); // Сохраняю роль чела.

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
    public String update(@RequestParam("id") String id, @ModelAttribute("user_with_id") @Valid User user, BindingResult bindingResult) {
        validator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) return "admin/update_user";


        userService.update(Integer.parseInt(id), user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") String id) {
        adminLogger.warning("Start destroy user and roles with him id!");
        int userId = Integer.parseInt(id); // Вынесу в отдельную переменную пропарсенный в целочисленный вид id-шник.
        User user = userService.readOne(userId); // Получу юзверя для передачи в метод удаление роли по владельцу.
        roleService.deleteByOwner(user); // Чистим в базе ролей по юзверю, ибо юзверь "велел жить долго".
        userService.delete(userId);
        return "redirect:/admin";
    }

    @PostMapping("/truncate_all")
    public String truncate() {
        userService.truncateAll();
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
        adminLogger.info("Owner id for update roles is -> " + id);
        adminLogger.info("All roles ? - " + marker);
        adminLogger.info("Roles of user -> " + rolesOfUser);
        adminLogger.info("Container -> " + container);
        return "admin/update_roles_of_user";
    }

    @PostMapping("/update_roles")
    public String updateRoles(@RequestParam("id") String id, @RequestParam("role") String futureRole) {
        int ownerId = Integer.parseInt(id); // Вынесу в отдельную переменную пропарсенный в целочисленный вид id-шник.
        User user = userService.readOne(ownerId); // Получаю юзверя и выношу для дальнейшей передачи в метод обновления юзверя.
        Role insertableRole = new Role(futureRole); // Присваемая роль юзверю.
        insertableRole.setOwner(user); // Определяю владельца присваемой роли.
        user.addRole(insertableRole); // Передаю роль в пул ролей юзверю.
        userService.update(ownerId, user); // Обновляю юзверя с новой ролью.
        roleService.create(insertableRole); // Заношу присвоенную роль в базу.
        adminLogger.info("\n\nUser: \n" + userService.readOne(ownerId) + "\nroles: " + userService.readOne(ownerId).getRoles() + "\n\n");
        return "redirect:/admin/update_roles_page?id=" + id;
    }

    @PostMapping("/delete_role")
    public String deleteRoleOfUser(@RequestParam("role_id") String roleId) {
        Role role = roleService.findOne(Integer.parseInt(roleId));
        User user = userService.readOne(role.getOwner().getId());
        user.getRoles().remove(role);
        userService.update(user.getId(), user);
        roleService.delete(role.getId());
        return "redirect:/admin/update_roles_page?id=" + user.getId();
    }


}
