package com.gongjakso.server.domain.apply.repository;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply,Long> {

    long countApplyWithStackNameUsingFetchJoinByPost(Post post);

    long countApplyWithStackNameUsingFetchJoinByPostAndApplyType(Post post,ApplyType applyType);

    boolean existsApplyByMemberAndPostAndIsCanceledIsFalse(Member member, Post post);

    Page<Apply> findAllByPost(Post post, Pageable pageable);
  
    List<Apply> findApplyByApplyTypeAndMemberAndIsCanceledFalse(ApplyType applyType, Member member);
  
    Page<Apply> findAllByMemberAndDeletedAtIsNullOrderByCreatedAtDesc(Member member, Pageable pageable);

    Apply findApplyByMemberAndPost(Member member,Post post);

    Optional<Apply> findApplyByApplyIdAndDeletedAtIsNull(Long applyId);
}
