package com.wak.chimplanet.entity;


import lombok.*;

import javax.persistence.*;

@Getter @Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "official_board_tag")
@Builder
public class OfficialBoardTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_tag_id", nullable = false)
    private Integer boardTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private OfficialBoard officialBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private TagObj tagObj;

    // 생성메서드
    public static OfficialBoardTag createOfficialBoardTag(TagObj tag, OfficialBoard officialBoard) {
        OfficialBoardTag officialBoardTag = OfficialBoardTag.builder()
                .officialBoard(officialBoard)
                .tagObj(tag)
                .build();

//        officialBoard.addOfficialBoardTag(officialBoardTag);

        return officialBoardTag;
    }

    public static OfficialBoardTag addOfficialBoardTag(TagObj tag, OfficialBoard officialBoard) {
        return OfficialBoardTag.builder()
                .officialBoard(officialBoard)
                .tagObj(tag)
                .build();
    }
}
