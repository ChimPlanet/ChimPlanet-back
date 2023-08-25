package com.wak.chimplanet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<?> handleUnauthorizedException() {
        ApiErrorResult errorResult = new ApiErrorResult(HttpStatus.UNAUTHORIZED.value(),
            "권한이 없는 게시글입니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResult);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<?> handleNotFoundException() {
        ApiErrorResult errorResult = new ApiErrorResult(HttpStatus.NOT_FOUND.value(),
            "해당 내용을 찾을 수 없습니다.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResult);
    }


}
