package com.wak.chimplanet.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tag")
@Getter
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
