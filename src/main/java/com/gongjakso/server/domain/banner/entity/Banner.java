package com.gongjakso.server.domain.banner.entity;

import com.gongjakso.server.domain.banner.enumerate.DomainType;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "banner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id", nullable = false, columnDefinition = "bigint")
    private Long bannerId;

    @Column(name = "domain_type", columnDefinition = "varchar(100)")
    @Enumerated(EnumType.STRING)
    private DomainType domainType;

    @Column(name = "image_url", columnDefinition = "text")
    private String imageUrl;

    @Column(name = "link_url", columnDefinition = "text")
    private String linkUrl;

    @Column(name = "priority", nullable = false, columnDefinition = "int")
    private Integer priority;

    @Column(name = "is_post", nullable = false, columnDefinition = "tinyint")
    private Boolean isPost;

    @Builder
    public Banner(String domainType, String imageUrl, String linkUrl, Integer priority) {
        this.domainType = DomainType.valueOf(domainType);
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.priority = priority;
        this.isPost = Boolean.TRUE;
    }
}
