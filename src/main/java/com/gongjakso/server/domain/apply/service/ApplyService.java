package com.gongjakso.server.domain.apply.service;

import com.gongjakso.server.domain.apply.dto.*;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.apply.repository.ApplyRepository;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.domain.post.enumerate.CategoryType;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import com.gongjakso.server.domain.post.repository.CategoryRepository;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.domain.post.repository.StackNameRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final StackNameRepository stackNameRepository;
    public void save(Member member, Long post_id, ApplyReq req){
        Post post = postRepository.findByPostId(post_id);
        if (post == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }else {
            //재지원 판단
            if(!applyRepository.existsApplyByMemberAndPost(member, post)){
                //지원 기간인지 판단
                if(post.getFinishDate().isBefore(LocalDateTime.now())){
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
            List<Category> categoryList = categoryRepository.findCategoryByPost(post);
            List<String> list = new ArrayList<>();
            if(categoryList!=null) {
                for (Category category : categoryList) {
                    list.add(String.valueOf(category.getCategoryType()));
                }
            }else {
                throw new ApplicationException(ErrorCode.NOT_FOUND_CATEGORY_EXCEPTION);
            }
            List<StackName> stackNameList = stackNameRepository.findStackNameByPost(post);
            List<String> stackList = new ArrayList<>();
            if(stackNameList!=null) {
                for (StackName stackName : stackNameList) {
                    stackList.add(String.valueOf(stackName.getStackNameType()));
                }
            }else {
                throw new ApplicationException(ErrorCode.NOT_FOUND_CATEGORY_EXCEPTION);
            }
            ApplyRes applyRes = ApplyRes.of(post,current_person, list, stackList);
            return applyRes;
        }
    }
    public ApplyPageRes applyListPage(long post_id,int page,int size){
        Post post = postRepository.findByPostId(post_id);
        if (post == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }else{
            Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdAt"));
            Page<Apply> applyPage = applyRepository.findAllByPost(post,pageable);
            List<ApplyList> applyLists = applyPage.getContent().stream()
                    .map(apply -> ApplyList.of(apply, decisionState(apply)))
                    .collect(Collectors.toList());
            int pageNo = applyPage.getNumber();
            int totalPages = applyPage.getTotalPages();
            boolean last = applyPage.isLast();
            return ApplyPageRes.of(applyLists,pageNo,size,totalPages,last);
        }
    }
    public ParticipationPageRes myParticipationPostListPage(int page,int size){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdAt"));
        Page<Apply> participationPage = applyRepository.findApplyByIsPass(true,pageable);
        List<ParticipationList> participationLists = participationPage.getContent().stream()
                .map(apply -> ParticipationList.of(apply.getPost(), CategoryType.valueOf(apply.getRecruit_part())))
                .collect(Collectors.toList());
        int pageNo = participationPage.getNumber();
        int totalPages = participationPage.getTotalPages();
        boolean last = participationPage.isLast();
        return ParticipationPageRes.of(participationLists,pageNo,size,totalPages,last);
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
        if (apply.getApplyType().equals(ApplyType.OPEN_APPLY)) {
            return "열람 완료";
        }else if (apply.getApplyType().equals(ApplyType.NOT_PASS)) {
            return "미선발";
        }
        else if (apply.getApplyType().equals(ApplyType.PASS)) {
            return "합류 완료";
        }
        return "미열람";
    }
    public void updateOpen(Long apply_id){
        Apply apply = applyRepository.findById(apply_id).orElseThrow(()->new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));
        apply.setApplyType(ApplyType.OPEN_APPLY);
    }
    public void updateState(Long apply_id, ApplyType applyType){
        Apply apply = applyRepository.findById(apply_id).orElseThrow(()->new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));
        //applyState 결정 했는지 판단
        if(apply.getApplyType().equals(ApplyType.NONE)||apply.getApplyType().equals(ApplyType.OPEN_APPLY)){
            apply.setApplyType(applyType);
            if(applyType.equals(ApplyType.PASS)){
                Post post = apply.getPost();
                //지원 파트 size 감소
                Category category = categoryRepository.findCategoryByPostAndCategoryType(post, CategoryType.valueOf(apply.getRecruit_part()));
                System.out.println("null"+",post:"+post+",categroy:"+apply.getRecruit_part());
                if(category.getSize()-1<=0){
                    throw new ApplicationException(ErrorCode.OVER_APPLY_EXCEPTION);
                }else {
                    category.setSize(category.getSize()-1);
                }
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
                post.setFinishDate(req.finishDate());
            }else {
                throw new ApplicationException(ErrorCode.NOT_RECRUITING_EXCEPION);
            }
        }
    }

    public ApplicationRes findApplication(Member member,Long apply_id,Long post_id){
        Apply apply = applyRepository.findById(apply_id).orElseThrow(()->new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));
        Post post = postRepository.findByPostId(post_id);
        if (post == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }else{
            //지원서 보기 권한
            if(post.getMember()==member){
                List<Category> categoryList = categoryRepository.findCategoryByPost(post);
                List<String> list = new ArrayList<>();
                if(categoryList!=null) {
                    for (Category category : categoryList) {
                        list.add(String.valueOf(category.getCategoryType()));
                    }
                }else {
                    throw new ApplicationException(ErrorCode.NOT_FOUND_CATEGORY_EXCEPTION);
                }
                List<StackName> stackNameList = stackNameRepository.findStackNameByPost(post);
                List<String> stackList = new ArrayList<>();
                if(stackNameList!=null) {
                    for (StackName stackName : stackNameList) {
                        stackList.add(String.valueOf(stackName.getStackNameType()));
                    }
                }else {
                    throw new ApplicationException(ErrorCode.NOT_FOUND_CATEGORY_EXCEPTION);
                }
                ApplicationRes applicationRes = ApplicationRes.of(apply,list, stackList);
                return applicationRes;
            }else {
                throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
            }
        }
    }

    public List<MyPageRes> getMyApplyList(Member member) {
        // Validation

        // Business Logic
        List<Apply> applyList = applyRepository.findAllByMemberAndDeletedAtIsNull(member);
        List<Long> postIdList = applyList.stream()
                .map(apply -> apply.getPost().getPostId())
                .toList();

        List<Post> postList = postRepository.findAllByPostIdInAndStatusAndDeletedAtIsNull(postIdList, RECRUITING);

        // Response
        return postList.stream()
                .map(post -> {
                    List<String> categoryList = post.getCategories().stream()
                            .map(category -> category.getCategoryType().toString())
                            .toList();

                    return MyPageRes.of(post, member, categoryList);
                })
                .collect(Collectors.toList());
    }
}
