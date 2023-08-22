package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RestController
public class TestErrorLogger {

    private static final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

    @GetMapping("/error-test")
    public void testErrorLog() {
        try {
            // Simulate an error
            int result = 10 / 0;
        } catch (Exception e) {
            errorLogger.error("Lỗi xảy ra trong quá trình xử lý: {}\n", e);
        }
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @GetMapping("/error-test-db")
    public void processAndLogHibernateError() {
        try {
            // Thực hiện một truy vấn Hibernate không hợp lệ để tạo ra lỗi
            entityManager.createQuery("SELECT t FROM NonExistingEntity t").getResultList();
        } catch (Exception e) {
            errorLogger.error("Lỗi Hibernate: {}\n", e.getMessage(), e);
        }
    }
}
