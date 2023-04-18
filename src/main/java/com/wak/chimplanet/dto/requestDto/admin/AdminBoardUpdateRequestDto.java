package com.wak.chimplanet.dto.requestDto.admin;

import com.wak.chimplanet.dto.requestDto.BoardTagRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminBoardUpdateRequestDto {

    @ApiModelProperty(value = "게시글 아이디", required = true, example = "10695952")
    @NotBlank(message = "게시글 아이디는 필수 입력 사항입니다.")
    private String articleId;

    @ApiModelProperty(value = "태그 정보 리스트", required = true)
    private List<BoardTagRequestDto> boardTags;

    @ApiModelProperty(value = "마감 여부 (ING/END)", required = true, example = "END")
    @NotBlank(message = "마감 여부는 필수 입력 사항입니다.")
    private String isEnd;

}
