package com.gongjakso.server.domain.contest.repository;

import com.gongjakso.server.domain.contest.dto.response.ContestListRes;
import com.gongjakso.server.domain.contest.dto.response.SearchContent;
import org.springframework.data.domain.Pageable;

public interface ContestRepositoryCustom {
    SearchContent searchList (String word, String sort, Pageable pageable);
}
