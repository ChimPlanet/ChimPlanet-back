package com.wak.chimplanet.dto.responseDto;

import com.wak.chimplanet.entity.TagObj;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagObjResponseDto {

    private Long tagId;
    private String tagName;
    private String parentTagId;
    private String childTagId;


    public TagObjResponseDto(TagObj tagObj) {
        this.tagId = tagObj.getTagId();
        this.tagName = tagObj.getTagName();
        this.parentTagId = tagObj.getParentTagId();
        this.childTagId = tagObj.getChildTagId();
    }

}
