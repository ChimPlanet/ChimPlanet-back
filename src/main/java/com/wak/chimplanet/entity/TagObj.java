package com.wak.chimplanet.entity;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tag_obj")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "태그 정보")
public class TagObj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id", nullable = false)
    private Long tagId;

    @Column(name = "tag_name", length = 50, nullable = false)
    private String tagName;

    @Column(name = "parent_tag_id", length = 50, nullable = false)
    private String parentTagId;

    @Column(name = "child_tag_id", length = 50, nullable = false)
    private String childTagId;

}