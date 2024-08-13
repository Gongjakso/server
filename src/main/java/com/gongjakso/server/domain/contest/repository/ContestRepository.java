package com.gongjakso.server.domain.contest.repository;

import com.gongjakso.server.domain.contest.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepository extends JpaRepository<Contest, Long> {

}
