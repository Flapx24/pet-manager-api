package com.example.demo.response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    
    public static <T> ResponseEntity<Map<String, Object>> success(T data) {
        return success(data, "Operation completed successfully");
    }
    
    public static <T> ResponseEntity<Map<String, Object>> success(T data, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static ResponseEntity<Map<String, Object>> success(String message) {
        return success(null, message);
    }
}