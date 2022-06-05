package com.github.pjm03.humansystem.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;

public class Response {
    public static ResponseEntity<?> success(Object data) {
        return success(HttpStatus.OK, data);
    }

    public static ResponseEntity<?> success(HttpStatus status, Object data) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("status", status.name());
        map.put("code", status.value());
        map.put("timestamp", System.currentTimeMillis());
        map.put("data", data);
        return new ResponseEntity<>(map, null, status);
    }

    public static ResponseEntity<?> fail(HttpStatus status, String errMsg) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("status", status.name());
        map.put("code", status.value());
        map.put("timestamp", System.currentTimeMillis());
        map.put("message", errMsg);
        return new ResponseEntity<>(map, null, status);
    }
}
