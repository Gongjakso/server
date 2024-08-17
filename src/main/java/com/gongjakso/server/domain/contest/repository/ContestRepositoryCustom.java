package com.gongjakso.server.domain.contest.repository;


import com.gongjakso.server.domain.contest.entity.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContestRepositoryCustom {
    Page<Contest> searchList (String word, String sort, Pageable pageable);
}
