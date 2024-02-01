package com.gongjakso.server.domain.apply.service;

import com.gongjakso.server.domain.apply.dto.ApplyList;
import com.gongjakso.server.domain.apply.dto.ApplyReq;
import com.gongjakso.server.domain.apply.dto.ApplicationRes;
import com.gongjakso.server.domain.apply.dto.ApplyRes;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.repository.ApplyRepository;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final PostRepository postRepository;
    public Apply save(Member member, Long post_id, ApplyReq req){
        Post post = postRepository.findByPostId(post_id);
        if (post == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }else {
            //재지원 판단
            if(!applyRepository.existsApplyByMemberAndPost(member, post)){
                //지원 기간인지 판단
                if(post.getEndDate().isBefore(LocalDateTime.now())){
                    throw new ApplicationException(ErrorCode.NOT_APPLY_EXCEPTION);
                }else {
                    Apply apply = req.toEntity(member, post);
                    return applyRepository.save(apply);
                }
            }else {
                throw new ApplicationException(ErrorCode.ALREADY_APPLY_EXCEPTION);
            }
        }
    }

    public ApplicationResponse<ApplyRes> findApply(Long post_id){
        Post post = postRepository.findById(post_id).orElseThrow(()->new NotFoundException("Post not found with id: " + post_id));
        int current_person = (int) applyRepository.countApplyByPost(post);
        List<Apply> applies = applyRepository.findAllByPost(post);
        List<ApplyList> applyLists = applies.stream()
                .map(apply -> ApplyList.of(apply, decisionState(apply)))
                .collect(Collectors.toList());
        ApplyRes applyRes = ApplyRes.of(post,current_person,applyLists);
        return ApplicationResponse.ok(applyRes);
    }
    private String decisionState(Apply apply){
        if(apply.getIs_decision()){
            if(apply.getIs_pass()) {
                return "합류 완료";
            }else {
                return "미선발";
            }
        }else {
            if(apply.getIs_open()){
                return "열람 완료";
            }else {
                return "미열람";
            }
        }

    }
    public ApplicationResponse<Void> updateOpen(Long apply_id){
        Apply apply = applyRepository.findById(apply_id).orElseThrow(()->new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));
        apply.setIs_open(true);
        return ApplicationResponse.ok();
    }
    public ApplicationResponse<Void> updateRecruit(Long apply_id, Boolean isRecruit){
        Apply apply = applyRepository.findById(apply_id).orElseThrow(()->new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));
        if(!apply.getIs_decision()){
            apply.setIs_pass(isRecruit);
            apply.setIs_decision(true);
            return ApplicationResponse.ok();
        }else {
            throw new ApplicationException(ErrorCode.ALREADY_DECISION_EXCEPION);
        }

    }
    public ApplicationResponse<ApplicationRes> findApplication(Long apply_id){
        Apply apply = applyRepository.findById(apply_id).orElseThrow(()->new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));
//        ApplicationRes applicationRes = ApplicationRes.builder().application(apply.getApplication()).recruit_part(apply.getRecruit_part()).build();
        ApplicationRes applicationRes = ApplicationRes.of(apply);
        return ApplicationResponse.ok(applicationRes);
    }
}
