package com.wak.chimplanet.portfolio.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio")
    private Long portfolio;

    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Column(name = "writer_name", nullable = false, length = 255)
    private String writerName;

    @Column(name = "portfolio_title", nullable = false, length = 255)
    private String portfolioTitle;

    @Column(name = "unauthorized", nullable = false, length = 1)
    private String unauthorized;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "is_end", nullable = false, length = 1)
    private String isEnd;

    @Column(name = "read_count", nullable = false)
    private int readCount;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate = LocalDateTime.now();

    @OneToMany
    @JoinColumn(name = "portfolio_tag_id")
    private List<PortfolioTag> portfolioTags = new ArrayList<>();
}
