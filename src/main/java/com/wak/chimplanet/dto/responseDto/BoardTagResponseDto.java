package com.wak.chimplanet.dto.responseDto;

import com.wak.chimplanet.entity.BoardTag;
import com.wak.chimplanet.entity.TagObj;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardTagResponseDto {

    private Long boardTagId;
    private TagObjResponseDto tagObjResponseDto;

    /* Entity -> DTO */
    public BoardTagResponseDto(BoardTag boardTag) {
        this.boardTagId = boardTag.getBoardTagId();
        this.tagObjResponseDto = new TagObjResponseDto(boardTag.getTagObj());
    }

}
