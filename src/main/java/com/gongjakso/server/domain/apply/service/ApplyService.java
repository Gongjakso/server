package com.gongjakso.server.domain.apply.service;

import com.gongjakso.server.domain.apply.dto.AddApplyReq;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.repository.ApplyRepository;
import com.gongjakso.server.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    public Apply save(Member member,AddApplyReq req){
        Apply apply = req.toEntity(member);
        return applyRepository.save(apply);
    }
    public Apply findMemberApplication(Long memberId){
        return applyRepository.findByMemberId(memberId)
                .orElse(null);
    }
}
