package com.gongjakso.server.domain.contest.repository;

import com.gongjakso.server.domain.contest.dto.response.ContestListRes;
import org.springframework.data.domain.Pageable;

public interface ContestRepositoryCustom {
    ContestListRes searchList (String word, String sort, Pageable pageable);
}
