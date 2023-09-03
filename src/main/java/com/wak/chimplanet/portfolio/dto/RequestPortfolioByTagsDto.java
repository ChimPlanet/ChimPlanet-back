package com.wak.chimplanet.portfolio.dto;

import com.wak.chimplanet.entity.TagObj;
import java.util.List;
import lombok.Data;

@Data
public class RequestPortfolioByTagsDto {
    private List<TagObj> tagObjs;
}
