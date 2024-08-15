package com.gongjakso.server.domain.email.entity;

import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@Table(name = "email")
@SQLDelete(sql = "UPDATE email SET deleted_at = NOW() where id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @Column(name = "address", nullable = false, columnDefinition = "text")
    private String address;

    // 이메일 수신 동의 여부 (추후, 미동의로 바뀔 경우 대비)
    @Column(name = "is_receive", nullable = false, columnDefinition = "tinyint")
    private Boolean isReceive;

    @Builder
    public Email(String address) {
        this.address = address;
        this.isReceive = Boolean.TRUE;
    }
}
