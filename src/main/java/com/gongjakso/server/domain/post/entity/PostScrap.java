package com.gongjakso.server.domain.post.entity;

import com.gongjakso.server.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrap{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postScrapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="postId")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberId")
    private Member member;

    @Column(name="scrap_status", columnDefinition = "tinyint" )
    private boolean scrapStatus;

    @Builder
    public PostScrap(Post post, Member member, Boolean scrapStatus){
        this.post = post;
        this.member = member;
        this.scrapStatus = scrapStatus;
    }

    public Boolean getScrapStatus(){
        return scrapStatus;
    }
}
