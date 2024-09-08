package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.team.dto.ApplyRes;
import com.gongjakso.server.domain.team.entity.Apply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ApplyRepositoryCustom {
    Page<ApplyRes> findByMemberAndPage(Member member, Pageable pageable);
    Optional<Apply> findApplyWithTeam(Long applyId);
}
