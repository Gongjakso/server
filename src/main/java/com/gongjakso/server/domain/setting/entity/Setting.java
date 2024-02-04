package com.gongjakso.server.domain.setting.entity;

import com.gongjakso.server.domain.setting.enumerate.DomainType;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "setting")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Setting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id", nullable = false, columnDefinition = "bigint")
    private Long settingId;

    @Column(name = "domain_type", columnDefinition = "varchar(100)")
    @Enumerated(EnumType.STRING)
    private DomainType domainType;

    @Column(name = "image_url", columnDefinition = "text")
    private String imageUrl;

    @Column(name = "link_url", columnDefinition = "text")
    private String linkUrl;

    @Builder
    public Setting(String domainType, String imageUrl, String linkUrl) {
        this.domainType = DomainType.valueOf(domainType);
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
    }
}
