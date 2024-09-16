package com.gongjakso.server.domain.apply.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.apply.dto.response.ApplyRes;
import com.gongjakso.server.domain.apply.entity.Apply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ApplyRepositoryCustom {
    Page<ApplyRes> findByMemberAndPage(Member member, Pageable pageable);
    Optional<Apply> findApplyDetails(Long applyId);
}
