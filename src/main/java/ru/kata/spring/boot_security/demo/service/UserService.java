package ru.kata.spring.boot_security.demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    public void create(String role, User user) {
        Role insertableRole = roleService.findByValueOfRole(role); // Передаваемая роль.
        insertableRole.addOwner(user); // "Сетаю" владельца роли.
        user.addRole(insertableRole); // Передаю роль челу.
        userRepository.save(user); // Сохраняю чела.
        roleService.update(insertableRole.getId(), insertableRole); // Сохраняю роль чела.
    }

    @Transactional
    public void nativeCreate(User user) {
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
            userRepository.save(updatedUser);
        }

    }

    @Transactional
    public void updateFields(int id, User user){
        user.setRoles(readOne(user.getId()).getRoles());
        update(id, user);
    }

    @Transactional
    public void updateRoles(int ownerId, String futureRole) {
        User user = userRepository.getReferenceById(ownerId); // Получаю юзверя и выношу для дальнейшей передачи в метод обновления юзверя.
        Role insertableRole = roleService.findByValueOfRole(futureRole); // Присваемая роль юзверю.
        insertableRole.addOwner(user); // Определяю владельца присваемой роли.
        roleService.update(insertableRole.getId(), insertableRole);
        user.addRole(insertableRole); // Передаю роль в пул ролей юзверю.
        update(ownerId, user); // Обновляю юзверя с новой ролью.
        roleService.create(insertableRole); // Заношу присвоенную роль в базу.
    }

    @Transactional
    public void takeAwayRoleOfUser(String roleId, String ownerId) {
        Role role = roleService.findOne(Integer.parseInt(roleId));
        User user = readOne(Integer.parseInt(ownerId));
        user.getRoles().remove(role);
        role.getOwners().remove(user);
        update(user.getId(), user);
        roleService.update(role.getId(), role);
    }

    @Transactional
    public void delete(int id) {
        User user = userRepository.getReferenceById(id); // Получу юзверя для передачи в метод удаление роли по владельцу.

        // Очищу из памяти ролей бывшего владельца перед его удалением.
        for (Role r : user.getRoles()) {
            Role role = roleService.findOne(r.getId());
            role.removeOwnerById(id);
            roleService.update(r.getId(), role);
        }

        // Так как просят ленивый fetch, то надо подгружать связанные сущности.
        roleService
                .readAll()
                .stream()
                .map(Role::getOwners)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet())
                .forEach(System.out::println);

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByName(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("404");
        return user.get();
    }
}