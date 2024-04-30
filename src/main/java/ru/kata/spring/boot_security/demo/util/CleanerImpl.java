package ru.kata.spring.boot_security.demo.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class CleanerImpl implements Cleaner {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void truncateAll() {
        entityManager.createNativeQuery("truncate table user").executeUpdate();
        entityManager.createNativeQuery("truncate table role").executeUpdate();
        entityManager.flush();
    }
}
