package com.gongjakso.server.domain.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "stack_name")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StackName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stack_name_id", nullable = false, columnDefinition = "bigint")
    private Long stackNameId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, columnDefinition = "bigint")
    @JsonIgnore
    private Post post;

    @Column(name = "stack_name_type", nullable = false, columnDefinition = "varchar(20)")
    private String stackNameType;

    @Builder
    public StackName(Post post, String stackNameType) {
        this.post = post;
        this.stackNameType = stackNameType;
    }
}
