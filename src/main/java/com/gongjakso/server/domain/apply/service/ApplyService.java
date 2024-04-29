package com.gongjakso.server.domain.apply.service;

import com.gongjakso.server.domain.apply.dto.*;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.entity.ApplyStack;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.apply.repository.ApplyRepository;
import com.gongjakso.server.domain.apply.repository.ApplyStackRepository;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.domain.post.enumerate.CategoryType;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import com.gongjakso.server.domain.post.enumerate.StackNameType;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.gongjakso.server.domain.post.enumerate.PostStatus.EXTENSION;
import static com.gongjakso.server.domain.post.enumerate.PostStatus.RECRUITING;
import static com.gongjakso.server.global.exception.ErrorCode.INVALID_VALUE_EXCEPTION;


@Service
@Transactional
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final StackNameRepository stackNameRepository;
    private final ApplyStackRepository applyStackRepository;

    public void save(Member member, Long post_id, ApplyReq req) {
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(post_id);
        if(post==null){
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }
        //Check reapply
        if (applyRepository.existsApplyByMemberAndPost(member, post)) {
            throw new ApplicationException(ErrorCode.ALREADY_APPLY_EXCEPTION);
        }
        //Check Post Date
        if ((!(post.getStatus() == RECRUITING))&&(!(post.getStatus() == EXTENSION))) {
            throw new ApplicationException(ErrorCode.NOT_APPLY_EXCEPTION);
        }


        Apply apply = req.toEntity(member, post);
        applyRepository.save(apply);
        if(post.isPostType()){
            System.out.println("project");
            for(String stackNameType : req.stack()){
                //StackNameType인지 판단
                if (!StackNameType.isValid(stackNameType)){
                    throw new ApplicationException(INVALID_VALUE_EXCEPTION);
                }
                StackName stackName = stackNameRepository.findStackNameByPostAndStackNameType(post,stackNameType);
                ApplyStack applyStack = ApplyStack.builder().apply(apply).stackName(stackName).build();
                applyStackRepository.save(applyStack);
            }
        }
    }

    public ApplyRes findApply(Member member,Long post_id) {
        //Get Post
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(post_id);
        if (post == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }
        //Check leader
        if (!Objects.equals(post.getMember().getMemberId(), member.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        //Change List Type
        List<String> categoryList = changeCategoryType(post);

        int current_person = (int) applyRepository.countApplyWithStackNameUsingFetchJoinByPost(post);
        return ApplyRes.of(post, current_person, categoryList);
    }

    public CategoryRes findPostCategory(Long post_id) {
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(post_id);
        if(post==null){
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }

        //Change List Type
        List<String> categoryList = changeCategoryType(post);
        List<String> stackNameList;
        if(post.isPostType()){
            stackNameList = changeStackNameType(post);
        }else {
            stackNameList= null;
        }

        return CategoryRes.of(categoryList, stackNameList);

    }

    public ApplicationRes findApplication(Member member, Long apply_id, Long post_id) {
        Apply apply = applyRepository.findById(apply_id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(post_id);
        if(post==null){
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }

        //Check leader
        if (!Objects.equals(post.getMember().getMemberId(), member.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        //Change List Type
        List<String> categoryList = changeCategoryType(post);
        List<String> stackNameList;
        List<String> applyStackList = null;
        if(post.isPostType()){
            stackNameList = changeStackNameType(post);
            System.out.println("change stack name");
            List<ApplyStack> applyStacks = applyStackRepository.findAllByApply(apply);
            applyStackList = new ArrayList<>();
            for(ApplyStack applyStack : applyStacks){
                applyStackList.add(applyStack.getStackName().getStackNameType());
            }
        }else {
            stackNameList= null;
        }

        return ApplicationRes.of(apply, categoryList, stackNameList,applyStackList);


    }

    public List<String> changeCategoryType(Post post){
        List<Category> categoryList = categoryRepository.findCategoryByPost(post);
        if (categoryList == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_CATEGORY_EXCEPTION);
        }

        List<String> stringTypelist = new ArrayList<>();
        for (Category category : categoryList) {
            stringTypelist.add(String.valueOf(category.getCategoryType()));
        }

        return stringTypelist;
    }

    public List<String> changeStackNameType(Post post){
        List<StackName> stackNameList = stackNameRepository.findStackNameByPost(post);
        List<String> stringTypelist = new ArrayList<>();
        for (StackName stackName : stackNameList) {
            stringTypelist.add(String.valueOf(stackName.getStackNameType()));
        }

        return stringTypelist;
    }

    public ApplyPageRes applyListPage(Member member,long post_id, int page, int size) {
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(post_id);
        if(post==null){
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }
        //Check leader
        if (!Objects.equals(post.getMember().getMemberId(), member.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Apply> applyPage = applyRepository.findAllByPost(post, pageable);
        List<ApplyList> applyLists = applyPage.getContent().stream()
                .map(apply -> ApplyList.of(apply, decisionState(apply)))
                .collect(Collectors.toList());
        int pageNo = applyPage.getNumber();
        int totalPages = applyPage.getTotalPages();
        boolean last = applyPage.isLast();

        return ApplyPageRes.of(applyLists, pageNo, size, totalPages, last);
    }

    public ParticipationPageRes myParticipationPostListPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Apply> participationPage = applyRepository.findApplyByApplyType(ApplyType.PASS, pageable);
        List<ParticipationList> participationLists = participationPage.getContent().stream()
                .map(apply -> ParticipationList.of(apply.getPost(), CategoryType.valueOf(apply.getRecruit_part())))
                .collect(Collectors.toList());
        int pageNo = participationPage.getNumber();
        int totalPages = participationPage.getTotalPages();
        boolean last = participationPage.isLast();
        return ParticipationPageRes.of(participationLists, pageNo, size, totalPages, last);
    }

    private String decisionState(Apply apply) {
        if (apply.getApplyType().equals(ApplyType.OPEN_APPLY)) {
            return "열람 완료";
        } else if (apply.getApplyType().equals(ApplyType.NOT_PASS)) {
            return "미선발";
        } else if (apply.getApplyType().equals(ApplyType.PASS)) {
            return "합류 완료";
        }
        return "미열람";
    }

    public void updateState(Member member,Long apply_id, ApplyType applyType) {
        Apply apply = applyRepository.findById(apply_id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));
        //Check ApplyType
        if (apply.getApplyType().equals(ApplyType.NONE) || apply.getApplyType().equals(ApplyType.OPEN_APPLY)) {
            throw new ApplicationException(ErrorCode.ALREADY_DECISION_EXCEPION);
        }

        apply.setApplyType(applyType);
        if (applyType.equals(ApplyType.PASS)) {
            Post post = apply.getPost();
            //Check leader
            if (!Objects.equals(post.getMember().getMemberId(), member.getMemberId())) {
                throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
            }
            Category category = categoryRepository.findCategoryByPostAndCategoryType(post, CategoryType.valueOf(apply.getRecruit_part()));
            if (category.getSize() - 1 <= 0) {
                throw new ApplicationException(ErrorCode.OVER_APPLY_EXCEPTION);
            } else {
                category.setSize(category.getSize() - 1);
            }
        }


    }

    public void updatePostState(Member member,Long post_id, PostStatus postStatus) {
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(post_id);
        if(post==null){
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }
        //Check leader
        if (!Objects.equals(post.getMember().getMemberId(), member.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        //CHECK POST STATUS
        if ((!(post.getStatus() == RECRUITING))&&(!(post.getStatus() == EXTENSION))) {
            throw new ApplicationException(ErrorCode.NOT_RECRUITING_EXCEPION);
        }

        post.setStatus(postStatus);
    }

    public void updatePostPeriod(Member member,Long post_id, PeriodReq req) {
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(post_id);
        if(post==null){
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }
        //Check leader
        if (!Objects.equals(post.getMember().getMemberId(), member.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        //Check Post Status
        if ((!(post.getStatus() == RECRUITING))&&(!(post.getStatus() == EXTENSION))) {
            throw new ApplicationException(ErrorCode.NOT_RECRUITING_EXCEPION);
        }
        post.setStatus(EXTENSION);
        post.setFinishDate(req.finishDate());
    }


    public List<MyPageRes> getMyApplyList(Member member) {
        // Validation

        // Business Logic
        List<Apply> applyList = applyRepository.findAllByMemberAndDeletedAtIsNull(member);


        // Response
        return applyList.stream()
                .filter(apply -> apply.getPost().getStatus() == PostStatus.RECRUITING ||
                        apply.getPost().getStatus() == PostStatus.EXTENSION)
                .map(apply -> {
                    Post post = apply.getPost();
                    List<String> categoryList = post.getCategories().stream()
                            .map(category -> category.getCategoryType().toString())
                            .toList();

                    return MyPageRes.of(post, apply, categoryList);
                })
                .collect(Collectors.toList());
    }

    public ApplicationRes getMyApplication(Member member, Long postId){
        Post post = postRepository.findByPostId(postId);
        if (post == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }else{
            Apply apply = applyRepository.findApplyByMemberAndPost(member, post);
            if(apply == null){
                throw new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION);
            }else {
                //Change List Type
                List<String> categoryList = changeCategoryType(post);
                List<String> stackNameList;
                List<String> applyStackList = null;
                if(post.isPostType()){
                    stackNameList = changeStackNameType(post);
                    System.out.println("change stack name");
                    List<ApplyStack> applyStacks = applyStackRepository.findAllByApply(apply);
                    applyStackList = new ArrayList<>();
                    for(ApplyStack applyStack : applyStacks){
                        applyStackList.add(applyStack.getStackName().getStackNameType());
                    }
                }else {
                    stackNameList= null;
                }

                return ApplicationRes.of(apply, categoryList, stackNameList,applyStackList);
            }
        }
    }
}
