package com.gongjakso.server.domain.apply.repository;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply,Long> {
    long countApplyByPost(Post post);
    List<Apply> findAllByPost(Post post);
    List<Apply> findAllByMemberAndPost(Member member, Post post);
}