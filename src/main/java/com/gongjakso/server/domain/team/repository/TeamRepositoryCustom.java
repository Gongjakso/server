package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.team.dto.response.SimpleTeamRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamRepositoryCustom {

    Page<SimpleTeamRes> findPagination(Long contestId, String province, String district, Pageable pageable);
}
