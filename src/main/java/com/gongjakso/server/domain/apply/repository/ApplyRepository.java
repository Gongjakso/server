package com.gongjakso.server.domain.apply.repository;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply,Long> {

    long countApplyWithStackNameUsingFetchJoinByPost(Post post);

    long countApplyWithStackNameUsingFetchJoinByPostAndApplyType(Post post,ApplyType applyType);

    boolean existsApplyByMemberAndPostAndIsCanceledIsFalse(Member member, Post post);

    Page<Apply> findAllByPost(Post post, Pageable pageable);
  
    List<Apply> findApplyByApplyTypeAndMemberAndIsCanceledFalse(ApplyType applyType, Member member);

    @Query(value = "SELECT a FROM Apply a JOIN FETCH a.post p JOIN FETCH p.categories WHERE a.member = :member AND a.post.status IN :postStatus AND a.deletedAt IS NULL AND a.isCanceled = false ORDER BY a.createdAt DESC",
            countQuery = "SELECT COUNT(DISTINCT a) FROM Apply a WHERE a.member = :member AND a.post.status IN :postStatus AND a.deletedAt IS NULL AND a.isCanceled = false")
    Page<Apply> findAllByMemberAndPostStatusInAndDeletedAtIsNullAndIsCanceledFalseOrderByCreatedAtDesc(@Param("member") Member member, @Param("postStatus") List<PostStatus> postStatusList, Pageable pageable);

    Apply findApplyByMemberAndPost(Member member,Post post);

    Optional<Apply> findApplyByApplyIdAndDeletedAtIsNull(Long applyId);
}
