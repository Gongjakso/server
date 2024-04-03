package com.gongjakso.server.domain.apply.service;

import com.gongjakso.server.domain.apply.dto.*;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.apply.enumerate.StackType;
import com.gongjakso.server.domain.apply.repository.ApplyRepository;
import com.gongjakso.server.domain.banner.entity.Banner;
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

    public void save(Member member, Long post_id, ApplyReq req) {
        Post post = postRepository.findById(post_id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION));
        //Check reapply
//        if (applyRepository.existsApplyByMemberAndPost(member, post)) {
//            throw new ApplicationException(ErrorCode.ALREADY_APPLY_EXCEPTION);
//        }
        //Check Post Date
//        if (post.getFinishDate().isBefore(LocalDateTime.now())) {
//            throw new ApplicationException(ErrorCode.NOT_APPLY_EXCEPTION);
//        }


        Apply apply = req.toEntity(member, post);
        applyRepository.save(apply);

    }

    public ApplyRes findApply(Long post_id) {
        //Get Post
        Post post = postRepository.findById(post_id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION));
        if (post == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION);
        }

        //Change List Type
        List<String> categoryList = changeCategoryType(post);
        List<String> stackNameList = changeStackNameType(post);

        int current_person = (int) applyRepository.countApplyByPost(post);
        return ApplyRes.of(post, current_person, categoryList, stackNameList);
    }

    public CategoryRes findPostCategory(Long post_id) {
        Post post = postRepository.findByPostId(post_id);

        //Change List Type
        List<String> categoryList = changeCategoryType(post);
        List<String> stackNameList = changeStackNameType(post);

        return CategoryRes.of(categoryList, stackNameList);

    }

    public ApplicationRes findApplication(Member member, Long apply_id, Long post_id) {
        Apply apply = applyRepository.findById(apply_id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));
        Post post = postRepository.findById(post_id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION));

        //Check leader
        if (post.getMember() != member) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        //Change List Type
        List<String> categoryList = changeCategoryType(post);
        List<String> stackNameList = changeStackNameType(post);

        return ApplicationRes.of(apply, categoryList, stackNameList);


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
        if(stackNameList==null){
            return null;
        }
        List<String> stringTypelist = new ArrayList<>();
        for (StackName stackName : stackNameList) {
            stringTypelist.add(String.valueOf(stackName.getStackNameType()));
        }

        return stringTypelist;
    }

    public ApplyPageRes applyListPage(long post_id, int page, int size) {
        Post post = postRepository.findById(post_id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION));

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

    public void updateState(Long apply_id, ApplyType applyType) {
        Apply apply = applyRepository.findById(apply_id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));
        //Check ApplyType
        if (apply.getApplyType().equals(ApplyType.NONE) || apply.getApplyType().equals(ApplyType.OPEN_APPLY)) {
            throw new ApplicationException(ErrorCode.ALREADY_DECISION_EXCEPION);
        }

        apply.setApplyType(applyType);
        if (applyType.equals(ApplyType.PASS)) {
            Post post = apply.getPost();
            Category category = categoryRepository.findCategoryByPostAndCategoryType(post, CategoryType.valueOf(apply.getRecruit_part()));
            if (category.getSize() - 1 <= 0) {
                throw new ApplicationException(ErrorCode.OVER_APPLY_EXCEPTION);
            } else {
                category.setSize(category.getSize() - 1);
            }
        }


    }

    public void updatePostState(Long post_id, PostStatus postStatus) {
        Post post = postRepository.findById(post_id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION));
        //CHECK POST STATUS
        if (!(post.getStatus() == RECRUITING)) {
            throw new ApplicationException(ErrorCode.NOT_RECRUITING_EXCEPION);
        }

        post.setStatus(postStatus);
    }

    public void updatePostPeriod(Long post_id, PeriodReq req) {
        Post post = postRepository.findById(post_id).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION));
        //Check Post Status
        if (!(post.getStatus() == RECRUITING)) {
            throw new ApplicationException(ErrorCode.NOT_RECRUITING_EXCEPION);
        }

        post.setFinishDate(req.finishDate());
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
