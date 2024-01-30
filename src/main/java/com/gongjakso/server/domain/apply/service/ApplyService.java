package com.gongjakso.server.domain.apply.service;

import com.gongjakso.server.domain.apply.dto.AddApplyReq;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.repository.ApplyRepository;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    public Apply save(Member member, Post post_id,AddApplyReq req){
        Apply apply = req.toEntity(member,post_id);
        return applyRepository.save(apply);
    }
    public Apply findMemberApplication(Long memberId){
//        return applyRepository.findByMemberId(memberId)
//                .orElse(null);
        return null;
    }
}
