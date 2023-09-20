package com.wak.chimplanet.exception;

import lombok.Getter;
import java.time.LocalDateTime;


@Getter
public class ApiErrorResult {
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;

    public ApiErrorResult(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}