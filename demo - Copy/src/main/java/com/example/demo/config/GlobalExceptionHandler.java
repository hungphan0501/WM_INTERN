package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

    @ExceptionHandler(Exception.class)
    public void handleGlobalException(Exception ex) {
        errorLogger.error("Lỗi toàn cục: {}\n", ex.getMessage(), ex);
        // Có thể thêm các xử lý khác tại đây, ví dụ: throw new RuntimeException("Lỗi toàn cục", ex);
    }
    @ExceptionHandler(DataAccessException.class)
    public void handleDataAccessException(DataAccessException ex) {
        errorLogger.error("Lỗi kết nối cơ sở dữ liệu: {}\n", ex.getMessage(), ex);
        // Có thể thêm các xử lý khác tại đây, ví dụ: throw new RuntimeException("Lỗi kết nối cơ sở dữ liệu", ex);
    }
}