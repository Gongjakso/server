package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.team.dto.response.TeamRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamRepositoryCustom {

    Page<TeamRes> findPagination(String province, String district, Pageable pageable);
}
