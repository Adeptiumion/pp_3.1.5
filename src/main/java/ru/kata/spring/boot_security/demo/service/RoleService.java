package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
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
        roleRepository.deleteByOwner(user);
    }

    public Role findByOwner(User user){
        return roleRepository.findByOwner(user);
    }
    public Role findOne(int id){
        return roleRepository.findById(id).orElse(null);
    }

}
