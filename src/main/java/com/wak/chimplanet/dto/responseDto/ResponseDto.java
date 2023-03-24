package com.wak.chimplanet.dto.responseDto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseDto<T> {

    private String message;
    private HttpStatus status;
    private Object data;
}
