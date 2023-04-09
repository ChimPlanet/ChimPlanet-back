package com.wak.chimplanet.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardTagRequestDto {

    private String articleId;
    private String parentTagId;
    private String childTagId;

}