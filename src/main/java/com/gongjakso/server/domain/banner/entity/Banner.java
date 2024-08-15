package com.gongjakso.server.domain.banner.entity;

import com.gongjakso.server.domain.banner.dto.request.BannerReq;
import com.gongjakso.server.domain.banner.enumerate.DomainType;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@Table(name = "banner")
@SQLDelete(sql = "UPDATE banner SET deleted_at = NOW() where id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

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

    public void update(BannerReq bannerReq, String imageUrl) {
        this.domainType = bannerReq.domainType();
        this.imageUrl = (!imageUrl.isEmpty()) ? imageUrl : this.imageUrl;
        this.linkUrl = bannerReq.linkUrl();
        this.priority = bannerReq.priority();
        this.isPost = bannerReq.isPost();
    }

    public void changeIsPost() {
        this.isPost = !this.isPost;
    }

    @Builder
    public Banner(DomainType domainType, String imageUrl, String linkUrl, Integer priority, Boolean isPost) {
        this.domainType = domainType;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.priority = priority;
        this.isPost = isPost;
    }
}
