package com.github.pjm03.humansystem.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class ApiResult<T> {
    @Schema(description = "Http 응답 상태", example = "OK")
    private final HttpStatus status;
    @Schema(description = "Http 응답 상태 코드", example = "200")
    private final int code;
    @Schema(description = "결과값 반환 시각", example = "1025098671")
    private final long timestamp;
    @Schema(description = "에러 메시지", example = "에러 메시지")
    private final String errMsg;
    @Schema(description = "반환 결과값")
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
