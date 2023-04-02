package com.wak.chimplanet.dto.responseDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class FileResponseDto<T> {

    @ApiModelProperty(value = "응답 코드", required = true)
    private String message;
    @ApiModelProperty(value = "응답 코드", required = true)
    private HttpStatus status;
    @ApiModelProperty(value = "응답 데이터")
    private Object data;

    public FileResponseDto() {
        this.status = HttpStatus.OK;
        this.message = "Success";
    }

}
