// src/main/java/com/reverb/app/services/DatabaseService.java
package com.reverb.app.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveChanges() {
        if (entityManager != null) {
            entityManager.flush();
        }
    }
}