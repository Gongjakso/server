package com.gongjakso.server.domain.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gongjakso.server.domain.post.enumerate.CategoryType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false, columnDefinition = "bigint")
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, columnDefinition = "bigint")
    @JsonIgnore
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
