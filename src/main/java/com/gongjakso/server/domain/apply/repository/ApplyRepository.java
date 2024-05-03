package com.gongjakso.server.domain.apply.repository;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply,Long> {
    long countApplyWithStackNameUsingFetchJoinByPost(Post post);
    long countApplyWithStackNameUsingFetchJoinByPostAndApplyType(Post post,ApplyType applyType);
    boolean existsApplyByMemberAndPost(Member member,Post post);
    Page<Apply> findAllByPost(Post post, Pageable pageable);
    Page<Apply> findApplyByApplyType(ApplyType applyType, Pageable pageable);
    List<Apply> findAllByMemberAndDeletedAtIsNull(Member member);
    Apply findApplyByMemberAndPost(Member member,Post post);
}
