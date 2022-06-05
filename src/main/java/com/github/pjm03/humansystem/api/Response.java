package com.github.pjm03.humansystem.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;

@RequiredArgsConstructor
@Getter
public class Response<T> {
    private final HttpStatus status;
    private final int code;
    private final long timestamp;
    private final String errMsg;
    private final T data;

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
