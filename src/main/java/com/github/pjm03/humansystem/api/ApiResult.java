package com.github.pjm03.humansystem.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class ApiResult<T> {
    private final HttpStatus status;
    private final int code;
    private final long timestamp;
    private final String errMsg;
    private final T data;

    public static <K> ApiResult<K> success(K data) {
        return success(HttpStatus.OK, data);
    }

    public static <K> ApiResult<K> success(HttpStatus status, K data) {
        return new ApiResult<>(status, status.value(), System.currentTimeMillis(), null, data);
    }

    public static ApiResult fail(HttpStatus status, String errMsg) {
        return new ApiResult<>(status, status.value(), System.currentTimeMillis(), errMsg, null);
    }
}
