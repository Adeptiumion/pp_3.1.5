package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("Select u from User u left join fetch u.roles where u.name=:user_name")
    Optional<User> findByName(@Param("user_name") String name);

    @Query("select u from User u left join fetch u.roles where u.email=:user_email")
    Optional<User> findByEmail(@Param("user_email") String name);

}