package ru.kata.spring.boot_security.demo.dto;

import lombok.*;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class UserDTO {
    private int id;
    private String name;
    private String lastName;
    private int age;
    private String email;
    private String password;
    private List<RoleDTO> roles;
}
