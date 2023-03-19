package com.wak.chimplanet.entity;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(description = "태그 정보")
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id", nullable = false)
    private Integer tagId;

    @Column(name = "tag_name", length = 50, nullable = false)
    private String tagName;

    @Column(name = "parent_tag_id", length = 50, nullable = false)
    private String parentTagId;

    @Column(name = "child_tag_id", length = 50, nullable = false)
    private String childTagId;

}
