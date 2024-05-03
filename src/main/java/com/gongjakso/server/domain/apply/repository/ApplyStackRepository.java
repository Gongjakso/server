package com.gongjakso.server.domain.apply.repository;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.entity.ApplyStack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplyStackRepository extends JpaRepository<ApplyStack,Long> {
    List<ApplyStack> findAllByApply(Apply apply);
}
