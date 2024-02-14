package com.gongjakso.server.domain.apply.service;

import com.gongjakso.server.domain.apply.dto.*;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.repository.ApplyRepository;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.enumerate.CategoryType;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import com.gongjakso.server.domain.post.repository.CategoryRepository;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gongjakso.server.domain.post.enumerate.PostStatus.RECRUITING;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    public void save(Member member, Long post_id, ApplyReq req){
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
                    applyRepository.save(apply);
                }
            }else {
                throw new ApplicationException(ErrorCode.ALREADY_APPLY_EXCEPTION);
            }
        }
    }

    public ApplyRes findApply(Long post_id){
        Post post = postRepository.findByPostId(post_id);
        if (post == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }else{
            int current_person = (int) applyRepository.countApplyByPost(post);
            List<Apply> applies = applyRepository.findAllByPost(post);
            List<ApplyList> applyLists = applies.stream()
                    .map(apply -> ApplyList.of(apply, decisionState(apply)))
                    .collect(Collectors.toList());
            ApplyRes applyRes = ApplyRes.of(post,current_person,applyLists);
            return applyRes;
        }
    }
    public PageRes applyListPage(long post_id,int pageNum){
        Post post = postRepository.findByPostId(post_id);
        if (post == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }else{
            int pageSize = 11;
            Pageable pageable = PageRequest.of(pageNum,pageSize, Sort.by(Sort.Direction.DESC,"createdAt"));
            Page<Apply> applyPage = applyRepository.findAllByPost(post,pageable);
            List<ApplyList> applyLists = applyPage.getContent().stream()
                    .map(apply -> ApplyList.of(apply, decisionState(apply)))
                    .collect(Collectors.toList());
            int pageNo = applyPage.getNumber();
            int totalPages = applyPage.getTotalPages();
            boolean last = applyPage.isLast();
            return PageRes.of(applyLists,pageNo,pageSize,totalPages,last);
        }
    }
    public CategoryRes findPostCategory(Long post_id){
        Post post = postRepository.findByPostId(post_id);
        if (post == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }else {
            List<Category> categoryList = categoryRepository.findCategoryByPost(post);
            List<String> list = new ArrayList<>();
            if(categoryList!=null){
                for (Category category : categoryList){
                    for(int i=0;i<category.getSize();i++){
                        list.add(String.valueOf(category.getCategoryType()));
                    }
                }
                CategoryRes categoryRes = new CategoryRes(list);
                return categoryRes;
            }else {
                throw new ApplicationException(ErrorCode.NOT_FOUND_CATEGORY_EXCEPTION);
            }
        }
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
    public void updateOpen(Long apply_id){
        Apply apply = applyRepository.findById(apply_id).orElseThrow(()->new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));
        apply.setIs_open(true);
    }
    public void updateRecruit(Long apply_id, Boolean isRecruit){
        Apply apply = applyRepository.findById(apply_id).orElseThrow(()->new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));
        if(!apply.getIs_decision()){
            apply.setIs_pass(isRecruit);
            apply.setIs_decision(true);
            Post post = apply.getPost();
            //지원 파트 size 감소
            Category category = categoryRepository.findCategoryByPostAndCategoryType(post, CategoryType.valueOf(apply.getRecruit_part()));
            System.out.println("null"+",post:"+post+",categroy:"+apply.getRecruit_part());
            if(category.getSize()-1<=0){
                throw new ApplicationException(ErrorCode.OVER_APPLY_EXCEPTION);
            }else {
                category.setSize(category.getSize()-1);
            }
        }else {
            throw new ApplicationException(ErrorCode.ALREADY_DECISION_EXCEPION);
        }

    }
    public void updatePostState(Long post_id,String state){
        Post post = postRepository.findByPostId(post_id);
        if (post == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }else{
            //공고 상태가 모집 중인지 판단
            if(post.getStatus()==RECRUITING){
                if(state.equals("close")){
                    post.setStatus(PostStatus.CLOSE);
                } else {
                    post.setStatus(PostStatus.CANCEL);
                }
            }else {
                throw new ApplicationException(ErrorCode.NOT_RECRUITING_EXCEPION);
            }
        }
    }
    public void updatePostPeriod(Long post_id, PeriodReq req) {
        Post post = postRepository.findByPostId(post_id);
        if (post == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }else {
            //공고 상태가 모집 중인지 판단
            if(post.getStatus()==RECRUITING){
                post.setEndDate(req.endDate());
            }else {
                throw new ApplicationException(ErrorCode.NOT_RECRUITING_EXCEPION);
            }
        }
    }

    public ApplicationRes findApplication(Long apply_id,Long post_id){
        Apply apply = applyRepository.findById(apply_id).orElseThrow(()->new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));
        Post post = postRepository.findByPostId(post_id);
        if (post == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }else{
            List<Category> categoryList = categoryRepository.findCategoryByPost(post);
            List<String> list = new ArrayList<>();
            if(categoryList!=null) {
                for (Category category : categoryList) {
                    for (int i = 0; i < category.getSize(); i++) {
                        list.add(String.valueOf(category.getCategoryType()));
                    }
                }
            }else {
                throw new ApplicationException(ErrorCode.NOT_FOUND_CATEGORY_EXCEPTION);
            }
            ApplicationRes applicationRes = ApplicationRes.of(apply,list);
            return applicationRes;
        }
    }
}
