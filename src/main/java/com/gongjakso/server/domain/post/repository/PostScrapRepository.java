package com.gongjakso.server.domain.post.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.PostScrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostScrapRepository extends JpaRepository<PostScrap, Long> {
    PostScrap findByPostAndMember(Post post, Member member);
    List<PostScrap> findByMemberAndScrapStatus(Member member,boolean scrapStatus);

    Page<PostScrap> findAllByMemberAndScrapStatusTrueOrderByPostScrapIdDesc(Member member, Pageable pageable);
}
