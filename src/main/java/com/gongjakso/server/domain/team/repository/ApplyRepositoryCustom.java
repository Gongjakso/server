package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.team.entity.Apply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplyRepositoryCustom {

    Boolean existsByMemberAndTeam(Long memberId, Long teamId);
    Page<Apply> findByMemberAndPage(Member member, Pageable pageable);
}
