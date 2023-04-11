package com.wak.chimplanet.dto.responseDto.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class AdminUpdateBoardResponseDto {

    @ApiModelProperty(value = "응답 코드", required = true)
    private String message;
    @ApiModelProperty(value = "응답 코드", required = true)
    private HttpStatus status;
    @ApiModelProperty(value = "응답 데이터")
    private Object data;

}
