package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.team.dto.response.SimpleTeamRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamRepositoryCustom {

    Page<SimpleTeamRes> findPaginationWithContest(Long contestId, String province, String district, Pageable pageable);

    Page<SimpleTeamRes> findPaginationWithoutContest(String province, String district, String keyword, Pageable pageable);

    Page<SimpleTeamRes> findRecruitPagination(Long memberId, Pageable pageable);

    Page<SimpleTeamRes> findApplyPagination(Long memberId, Pageable pageable);

    Page<SimpleTeamRes> findScrapPagination(Long memberId, Pageable pageable);
}
