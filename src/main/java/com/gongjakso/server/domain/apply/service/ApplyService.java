package com.gongjakso.server.domain.apply.service;

import com.gongjakso.server.domain.apply.dto.AddApplyReq;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.repository.ApplyRepository;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.domain.post.service.PostService;
import com.gongjakso.server.global.common.ApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final PostRepository postRepository;
    public Apply save(Member member, Long post_id,AddApplyReq req){
        Post post = postRepository.findByPostId(post_id);
        if (post == null) {
            // Handle the case where the Post entity with the given post_id is not found
            throw new NotFoundException("Post not found with id: " + post_id);
        }
        Apply apply = req.toEntity(member, post);
        return applyRepository.save(apply);
    }

    public ApplicationResponse<Void> updateOpen(Long apply_id){
        Apply apply = applyRepository.findById(apply_id).orElseThrow(()->new NotFoundException("Apply not found with id: " + apply_id));
        apply.setIs_open(true);
        return ApplicationResponse.ok();
    }
    public ApplicationResponse<Void> updateRecruit(Long apply_id){
        Apply apply = applyRepository.findById(apply_id).orElseThrow(()->new NotFoundException("Apply not found with id: " + apply_id));
        apply.setIs_pass(true);
        return ApplicationResponse.ok();
    }
    public Apply findMemberApplication(Long memberId){
//        return applyRepository.findByMemberId(memberId)
//                .orElse(null);
        return null;
    }
}
