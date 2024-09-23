package com.gongjakso.server.domain.contest.repository;


import com.gongjakso.server.domain.contest.entity.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContestRepositoryCustom {
    /**
     * 찾고자하는 단어 검색
     * @param word searchWord
     * @param sort 정렬순
     */
    Page<Contest> searchList (String word, String sort, Pageable pageable);


}
