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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.gongjakso.server.domain.post.enumerate.PostStatus.EXTENSION;
import static com.gongjakso.server.domain.post.enumerate.PostStatus.RECRUITING;
import static com.gongjakso.server.global.exception.ErrorCode.INVALID_VALUE_EXCEPTION;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final StackNameRepository stackNameRepository;
    private final ApplyStackRepository applyStackRepository;
//    private final EmailClient emailClient;

    @Transactional
    public void save(Member member, Long postId, ApplyReq req) {
        // Validation
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(postId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION));
        //Check reapply
        if (applyRepository.existsApplyByMemberAndPostAndIsCanceledIsFalse(member, post)) {
            throw new ApplicationException(ErrorCode.ALREADY_APPLY_EXCEPTION);
        }
        //Check Post Date
        if ((!(post.getStatus() == RECRUITING))&&(!(post.getStatus() == EXTENSION))) {
            throw new ApplicationException(ErrorCode.NOT_APPLY_EXCEPTION);
        }

        Apply apply = req.toEntity(member, post);
        applyRepository.save(apply);
        if(post.isPostType()){
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

    public ApplyRes findApply(Member member,Long postId) {
        //Get Post
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(postId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION));
        if (!Objects.equals(post.getMember().getMemberId(), member.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        //Change List Type
        List<String> categoryList = changeCategoryType(post);

        int current_person = (int) applyRepository.countApplyWithStackNameUsingFetchJoinByPostAndApplyType(post,ApplyType.PASS);
        return ApplyRes.of(post, current_person, categoryList);
    }

    public CategoryRes findPostCategory(Long postId) {
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(postId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION));

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

    public ApplicationRes findApplication(Member member, Long apply_id, Long postId) {
        Apply apply = applyRepository.findById(apply_id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(postId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION));

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

        return ApplicationRes.of(apply, categoryList, stackNameList, applyStackList);
    }

    @Transactional
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

    @Transactional
    public List<String> changeStackNameType(Post post){
        List<StackName> stackNameList = stackNameRepository.findStackNameByPost(post);
        List<String> stringTypelist = new ArrayList<>();
        for (StackName stackName : stackNameList) {
            stringTypelist.add(String.valueOf(stackName.getStackNameType()));
        }

        return stringTypelist;
    }

    public ApplyPageRes applyListPage(Member member, long postId, int page, int size) {
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(postId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION));
        if(!post.getMember().getMemberId().equals(member.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Apply> applyPage = applyRepository.findAllByPost(post, pageable);
        List<ApplyList> applyLists = applyPage.getContent().stream()
                .map(apply -> ApplyList.of(apply, decisionState(apply)))
                .collect(Collectors.toList());
        int pageNo = applyPage.getNumber();
        int totalPages = applyPage.getTotalPages();
        boolean last = applyPage.isLast();

        return ApplyPageRes.of(applyLists, pageNo, size, totalPages, last);
    }

    public ParticipationPageRes myParticipationPostListPage(Member member, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<Apply> applyList = applyRepository.findApplyByApplyTypeAndMemberAndIsCanceledFalse(ApplyType.PASS,member);
        List<Long> postIdList = applyList.stream()
                .filter(apply -> apply.getPost().getStatus().equals(PostStatus.ACTIVE) || apply.getPost().getStatus().equals(PostStatus.COMPLETE))
                .map(Apply::getApplyId)
                .toList();
        Page<Post> postPage = postRepository.findAllByPostIdInOrMember(postIdList, member, pageable);
        List<ParticipationList> participationLists = postPage.getContent().stream()
                .filter(post -> post.getDeletedAt() == null)
                .map(ParticipationList::of)
                .collect(Collectors.toList());
        int pageNo = postPage.getNumber();
        int totalPages = postPage.getTotalPages();
        boolean last = postPage.isLast();
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

    @Transactional
    public void updateState(Member member,Long apply_id, ApplyType applyType) {
        Apply apply = applyRepository.findById(apply_id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));
        //Check ApplyType
        if (apply.getApplyType().equals(ApplyType.NOT_PASS) || apply.getApplyType().equals(ApplyType.PASS)) {
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
            if (category.getSize() <= 0) {
                throw new ApplicationException(ErrorCode.OVER_APPLY_EXCEPTION);
            } else {
                category.setSize(category.getSize() - 1);
            }
        }
    }

    @Transactional
    public void updatePostState(Member member,Long postId, PostStatus postStatus) {
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(postId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION));
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

    @Transactional
    public void updatePostPeriod(Member member,Long postId, PeriodReq req) {
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(postId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION));
        if (!Objects.equals(post.getMember().getMemberId(), member.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        //Check Post Status
        if ((!(post.getStatus() == RECRUITING))&&(!(post.getStatus() == EXTENSION))) {
            throw new ApplicationException(ErrorCode.NOT_RECRUITING_EXCEPION);
        }
        post.setStatus(EXTENSION);
        post.setFinishDate(req.finishDate().atStartOfDay());
    }


    public Page<MyPageRes> getMyApplyList(Member member, Pageable pageable) {
        // Validation

        // Business Logic
        Page<Apply> applyPage = applyRepository.findAllByMemberAndDeletedAtIsNullOrderByCreatedAtDesc(member, pageable);
        List<MyPageRes> applyList = applyPage.stream()
                .filter(apply -> apply.getPost().getStatus() == PostStatus.RECRUITING ||
                        apply.getPost().getStatus() == PostStatus.EXTENSION ||
                        apply.getPost().getStatus() == PostStatus.CANCEL ||
                        apply.getPost().getStatus() == PostStatus.CLOSE)
                .map(apply -> {
                    Post post = apply.getPost();
                    List<String> categoryList = post.getCategories().stream()
                            .map(category -> category.getCategoryType().toString())
                            .toList();

                    return MyPageRes.of(post, apply, categoryList);
                })
                .toList();

        // Response
        return new PageImpl<>(applyList, pageable, applyPage.getTotalElements());
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

    @Transactional
    public PatchApplyRes cancelApply(Member member, Long applyId) {
        // Validation: 논리적 삭제 데이터이거나 신청자 본인이 아닌 경우에 대한 유효성 검증 + 공고의 모집 기한 확인
        Apply apply = applyRepository.findApplyByApplyIdAndDeletedAtIsNull(applyId).orElseThrow(() -> new ApplicationException(ErrorCode.ALREADY_DELETE_EXCEPTION));
        if (!apply.getMember().getMemberId().equals(member.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        Post post = postRepository.findByPostIdAndDeletedAtIsNull(apply.getPost().getPostId()).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION));
        if(post.getFinishDate().isBefore(LocalDateTime.now())) {
            throw new ApplicationException(ErrorCode.ALREADY_FINISH_EXCEPTION);
        }
        // Business Logic: isCanceled 칼럼을 TRUE로 변경하고, 공고 게시자에세 이메일을 전송한다.
        apply.updateIsCanceled(Boolean.TRUE);
        Apply saveApply = applyRepository.save(apply);
        // TODO: 이메일 발송 로직을 실시간성이 아닌 일괄배치 또는 비동기로 변환 필요 (성능 문제)
        // emailClient.sendOneEmail(post.getMember().getEmail());

        // Response
        return PatchApplyRes.of(saveApply, member);
    }
}
