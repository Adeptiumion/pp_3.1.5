package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import java.util.List;
import java.util.logging.Logger;

@Service
@Transactional(readOnly = true)
public class RoleService {
    private final Logger roleServiceLogger = Logger.getLogger(RoleService.class.getSimpleName());
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> readAll(){
        return roleRepository.findAll();
    }

    @Transactional
    public void create(Role role){
        roleRepository.save(role);
    }
    @Transactional
    public void delete(int id){
        roleRepository.deleteById(id);
    }

    @Transactional
    public void deleteByOwner(User user){
        roleRepository.deleteByOwners(user);
    }

    @Transactional
    public void update(int id, Role updatedRole){
        roleServiceLogger.info("Role is detected? -> " + roleIsDetected(id));
        if (roleIsDetected(id)){
            updatedRole.setId(id);
            roleRepository.save(updatedRole);
        }
    }

    public boolean roleIsDetected(int id){
        return roleRepository.findById(id).isPresent();
    }

    public Role findOne(int id){
        return roleRepository.findById(id).orElse(null);
    }

    public Role findByValueOfRole(String value){
        return roleRepository.findByValueOfRole(value);
    }


}
