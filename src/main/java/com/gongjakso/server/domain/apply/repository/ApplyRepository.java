package com.gongjakso.server.domain.apply.repository;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply,Long> {
    long countApplyByPost(Post post);
    boolean existsApplyByMemberAndPost(Member member,Post post);
    Page<Apply> findAllByPost(Post post, Pageable pageable);
    Page<Apply> findApplyByIsPass(Boolean IsPass, Pageable pageable);
}