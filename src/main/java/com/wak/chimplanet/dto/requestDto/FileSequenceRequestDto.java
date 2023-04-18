package com.wak.chimplanet.dto.requestDto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileSequenceRequestDto {

    private Long fileId;
    private Integer sequence;

}
