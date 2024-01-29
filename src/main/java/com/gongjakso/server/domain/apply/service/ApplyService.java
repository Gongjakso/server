package com.gongjakso.server.domain.apply.service;

import com.gongjakso.server.domain.apply.dto.AddApplyReq;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.repository.ApplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    public Apply save(AddApplyReq req){
        return applyRepository.save(req.toEntity());
    }
}
