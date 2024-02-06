package com.gongjakso.server.domain.post.entity;

import com.gongjakso.server.domain.post.enumerate.CategoryType;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false, columnDefinition = "bigint")
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false, columnDefinition = "bigint")
    private Post post;

    @Column(name = "category_type", nullable = false, columnDefinition = "varchar(100)")
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Column(name = "size", nullable = false, columnDefinition = "int")
    private Integer size;

    @Builder
    public Category(Post post, String categoryType, Integer size) {
        this.post = post;
        this.categoryType = CategoryType.valueOf(categoryType);
        this.size = size;
    }
}
