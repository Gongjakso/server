package com.gongjakso.server.domain.post.service;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.common.Pagination;
import com.gongjakso.server.domain.post.dto.*;
import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.PostScrap;
import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.domain.post.enumerate.CategoryType;
import com.gongjakso.server.domain.post.enumerate.StackNameType;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.domain.post.repository.PostScrapRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gongjakso.server.domain.post.enumerate.PostStatus.RECRUITING;
import static com.gongjakso.server.global.exception.ErrorCode.*;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostScrapRepository postScrapRepository;


    @Transactional
    public PostRes create(Member member, PostReq req) {
        Post entity = new Post(req.getTitle(), member, req.getContents(), req.getStatus(), req.getStartDate(), req.getEndDate(),
                req.getFinishDate(), req.getMaxPerson(), req.getMeetingMethod(), req.getMeetingArea(), req.isQuestionMethod(),
                req.getQuestionLink(), req.isPostType(), new ArrayList<>(), new ArrayList<>());

        List<StackName> stackNames = req.getStackNames().stream()
                .map(stackNameReq ->  new StackName(entity, stackNameReq.getStackNameType().toString()))
                .collect(Collectors.toList());
        entity.getStackNames().addAll(stackNames);

        List<Category> categories = req.getCategories().stream()
                .map(categoryReq ->  new Category(entity, categoryReq.getCategoryType().toString(), categoryReq.getSize()))
                .collect(Collectors.toList());
        entity.getCategories().addAll(categories);

        postRepository.save(entity);
        return PostRes.builder()
                .postId(entity.getPostId())
                .memberId(entity.getMember().getMemberId())
                .title(entity.getTitle())
                .contents(entity.getContents())
                .status(entity.getStatus())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .finishDate(entity.getFinishDate())
                .maxPerson(entity.getMaxPerson())
                .stackNames(entity.getStackNames())
                .categories(entity.getCategories())
                .meetingMethod(entity.getMeetingMethod())
                .meetingArea(entity.getMeetingArea())
                .questionMethod(entity.isQuestionMethod())
                .questionLink(entity.getQuestionLink())
                .postType(entity.isPostType())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    @Transactional
    public PostRes read(Member member, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_EXCEPTION));

        return PostRes.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .title(post.getTitle())
                .contents(post.getContents())
                .status(post.getStatus())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .maxPerson(post.getMaxPerson())
                .stackNames(post.getStackNames())
                .categories(post.getCategories())
                .meetingMethod(post.getMeetingMethod())
                .meetingArea(post.getMeetingArea())
                .questionMethod(post.isQuestionMethod())
                .questionLink(post.getQuestionLink())
                .postType(post.isPostType())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .deletedAt(post.getDeletedAt())
                .build();
    }

    @Transactional
    public PostRes modify(Member member, Long id, PostReq req) {
        Post entity = postRepository.findByPostIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_EXCEPTION));
        if(!member.getMemberId().equals(entity.getMember().getMemberId())){
            throw new ApplicationException(UNAUTHORIZED_EXCEPTION);
        }

        entity.getStackNames().clear();
        entity.getCategories().clear();
        List<StackName> updatedStackNames = req.getStackNames().stream()
                .map(stackNameReq ->  new StackName(entity, stackNameReq.getStackNameType().toString()))
                .collect(Collectors.toList());
        entity.getStackNames().addAll(updatedStackNames);

        List<Category> categories = req.getCategories().stream()
                .map(categoryReq ->  new Category(entity, categoryReq.getCategoryType().toString(), categoryReq.getSize()))
                .collect(Collectors.toList());
        entity.getCategories().addAll(categories);


        return PostRes.builder()
                .postId(entity.getPostId())
                .memberId(entity.getMember().getMemberId())
                .title(entity.getTitle())
                .contents(entity.getContents())
                .status(entity.getStatus())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .finishDate(entity.getFinishDate())
                .maxPerson(entity.getMaxPerson())
                .stackNames(entity.getStackNames())
                .categories(entity.getCategories())
                .meetingMethod(entity.getMeetingMethod())
                .meetingArea(entity.getMeetingArea())
                .questionMethod(entity.isQuestionMethod())
                .questionLink(entity.getQuestionLink())
                .postType(entity.isPostType())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .deletedAt(entity.getDeletedAt())
                .build();

    }

    @Transactional
    public PostDeleteRes delete(Member member, Long id) {
        Post entity = postRepository.findByPostIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_EXCEPTION));

        if(!member.getMemberId().equals(entity.getMember().getMemberId())){
            throw new ApplicationException(UNAUTHORIZED_EXCEPTION);
        }

        postRepository.delete(entity);
        return PostDeleteRes.builder()
                .postId(entity.getPostId())
                .memberId(entity.getMember().getMemberId())
                .build();
    }

    /*
    전체 공모전 공고 목록 조회
     */
    public Page<GetContestRes> getContests(String sort, Pageable p) throws ApplicationException {
        int page = p.getPageNumber();
        int size = p.getPageSize();
        try {
            Pagination pagination = new Pagination((int) postRepository.count(), page, size);
            Pageable pageable = PageRequest.of(pagination.getPage(), size);
            Page<Post> posts;
            if(sort.equals("createdAt,desc")){ //최신순
                posts = postRepository.findAllByPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByCreatedAtDesc(LocalDateTime.now(), RECRUITING, pageable);
            } else{ //스크랩순
                posts = postRepository.findAllByPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescCreatedAtDesc(LocalDateTime.now(), RECRUITING, pageable);
            }
            return posts.map(post -> new GetContestRes(
                    post.getPostId(),
                    post.getTitle(),
                    post.getMember().getName(),
                    post.getStatus(),
                    post.getStartDate(),
                    post.getFinishDate(),
                    post.getCategories(),
                    post.getScrapCount()
            ));
        } catch (Exception e) {
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
    }

    /*
   검색어 기반 공모전 공고 목록 조회
    */
    public Page<GetContestRes> getContestsBySearchWord(String sort, String searchWord, Pageable page) throws ApplicationException {
        try {
            Pagination pagination = new Pagination((int) postRepository.count(), page.getPageNumber(), page.getPageSize());
            Pageable pageable = PageRequest.of(pagination.getPage(), page.getPageSize());
            searchWord = searchWord.replaceAll(" ", ""); // 검색어에서 공백 제거
            Page<Post> posts;
            if (sort.equals("createdAt,desc")) {
                posts = postRepository.findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, pageable);
            } else{
                posts = postRepository.findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, pageable);
            }
            return posts.map(post -> new GetContestRes(
                    post.getPostId(),
                    post.getTitle(),
                    post.getMember().getName(),
                    post.getStatus(),
                    post.getStartDate(),
                    post.getFinishDate(),
                    post.getCategories(),
                    post.getScrapCount()
            ));
        } catch (Exception e) {
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
    }

    /*
    지역, 카테고리 기반 공모전 공고 목록 조회
     */
    public Page<GetContestRes> getContestsByMeetingAreaAndCategoryAndSearchWord(
            String sort, String meetingArea, String category, String searchWord, Pageable page) throws ApplicationException {
        try {
            Pagination pagination = new Pagination((int) postRepository.count(), page.getPageNumber(), page.getPageSize());
            Pageable pageable = PageRequest.of(pagination.getPage(), page.getPageSize());
            searchWord = searchWord.replaceAll(" ", "");
            if(!category.isBlank()) {
                if (!CategoryType.isValid(category)){
                    throw new ApplicationException(INVALID_VALUE_EXCEPTION);
                }
                Page<Post> posts;
                if (sort.equals("createdAt,desc")) {
                    posts = postRepository.findAllPostsJoinedWithCategoriesByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsAndCategoriesCategoryTypeContainsOrderByCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingArea, category.toString(), pageable);
                }else{
                    posts = postRepository.findAllPostsJoinedWithCategoriesByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsAndCategoriesCategoryTypeContainsOrderByScrapCountDescCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingArea, category.toString(), pageable);
                }
                return posts.map(post -> new GetContestRes(
                        post.getPostId(),
                        post.getTitle(),
                        post.getMember().getName(),
                        post.getStatus(),
                        post.getStartDate(),
                        post.getFinishDate(),
                        post.getCategories(),
                        post.getScrapCount()
                ));
            } else{
                Page<Post> posts;
                if (sort.equals("createdAt,desc")) {
                    posts = postRepository.findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsOrderByCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingArea, pageable);
                }else{
                    posts = postRepository.findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsOrderByScrapCountDescCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingArea, pageable);
                }
                return posts.map(post -> new GetContestRes(
                        post.getPostId(),
                        post.getTitle(),
                        post.getMember().getName(),
                        post.getStatus(),
                        post.getStartDate(),
                        post.getFinishDate(),
                        post.getCategories(),
                        post.getScrapCount()
                ));
            }
        } catch (Exception e) {
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
    }

    /*
    전체 프로젝트 공고 목록 조회
     */
    public Page<GetProjectRes> getProjects(String sort, Pageable p) throws ApplicationException {
        int page = p.getPageNumber();
        int size = p.getPageSize();
        try {
            Pagination pagination = new Pagination((int) postRepository.count(), page, size);
            Pageable pageable = PageRequest.of(pagination.getPage(), size);
            Page<Post> posts;
            if(sort.equals("createdAt,desc")){ //최신순
                posts = postRepository.findAllByPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByCreatedAtDesc(LocalDateTime.now(), RECRUITING, pageable);
            } else{ //스크랩순
                posts = postRepository.findAllByPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescCreatedAtDesc(LocalDateTime.now(), RECRUITING, pageable);
            }
            return posts.map(post -> new GetProjectRes(
                    post.getPostId(),
                    post.getTitle(),
                    post.getMember().getName(),
                    post.getStatus(),
                    post.getStartDate(),
                    post.getFinishDate(),
                    post.getCategories(),
                    post.getScrapCount()
            ));
        } catch (Exception e) {
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
    }

    /*
    검색어 기반 프로젝트 공고 목록 조회
     */
    public Page<GetProjectRes> getProjectsBySearchWord(String sort, String searchWord, Pageable page) throws ApplicationException {
        try {
            Pagination pagination = new Pagination((int) postRepository.count(), page.getPageNumber(), page.getPageSize());
            Pageable pageable = PageRequest.of(pagination.getPage(), page.getPageSize());
            searchWord = searchWord.replaceAll(" ", ""); // 검색어에서 공백 제거
            Page<Post> posts;
            if (sort.equals("createdAt,desc")) {
                posts = postRepository.findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, pageable);
            } else{
                posts = postRepository.findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, pageable);
            }
            return posts.map(post -> new GetProjectRes(
                    post.getPostId(),
                    post.getTitle(),
                    post.getMember().getName(),
                    post.getStatus(),
                    post.getStartDate(),
                    post.getFinishDate(),
                    post.getCategories(),
                    post.getScrapCount()
            ));
        } catch (Exception e) {
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
    }

    /*
    지역, 스택 기반 프로젝트 공고 목록 조회
     */
    public Page<GetProjectRes> getProjectsByMeetingAreaAndStackNameAndSearchWord(
            String sort, String meetingArea, String stackName, String searchWord, Pageable page) throws ApplicationException {
        try {
            Pagination pagination = new Pagination((int) postRepository.count(), page.getPageNumber(), page.getPageSize());
            Pageable pageable = PageRequest.of(pagination.getPage(), page.getPageSize());
            searchWord = searchWord.replaceAll(" ", "");
            if(!stackName.isBlank()) {
                if (!StackNameType.isValid(stackName)){
                    throw new ApplicationException(INVALID_VALUE_EXCEPTION);
                }
                Page<Post> posts;
                if (sort.equals("createdAt,desc")) {
                    posts = postRepository.findAllPostsJoinedWithStackNamesByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsAndStackNamesStackNameTypeContainsOrderByCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingArea, stackName.toString(), pageable);
                }else{
                    posts = postRepository.findAllPostsJoinedWithStackNamesByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsAndStackNamesStackNameTypeContainsOrderByScrapCountDescCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingArea, stackName.toString(), pageable);
                }
                return posts.map(post -> new GetProjectRes(
                    post.getPostId(),
                    post.getTitle(),
                    post.getMember().getName(),
                    post.getStatus(),
                    post.getStartDate(),
                    post.getFinishDate(),
                    post.getCategories(),
                    post.getScrapCount()
                ));
            } else{
                Page<Post> posts;
                if (sort.equals("createdAt,desc")) {
                    posts = postRepository.findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsOrderByCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingArea, pageable);
                }else{
                    posts = postRepository.findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsOrderByScrapCountDescCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingArea, pageable);
                }
                return posts.map(post -> new GetProjectRes(
                        post.getPostId(),
                        post.getTitle(),
                        post.getMember().getName(),
                        post.getStatus(),
                        post.getStartDate(),
                        post.getFinishDate(),
                        post.getCategories(),
                        post.getScrapCount()
                ));
            }
        } catch (Exception e) {
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
    }

    /*
        공고 스크랩 기능
     */
    @Transactional
    public void scrapPost(Member member, Long postId) {
        try {
            Post post = postRepository.findByPostIdAndDeletedAtIsNull(postId)
                    .orElseThrow(() -> new ApplicationException(NOT_FOUND_EXCEPTION));
            if (member.getMemberId() == null) {
                throw new ApplicationException(UNAUTHORIZED_EXCEPTION);
            }

            PostScrap postScrap = postScrapRepository.findByPostAndMember(post, member);

            if (postScrap == null) { //첫 스크랩
                postScrap = PostScrap.builder()
                        .post(post)
                        .member(member)
                        .scrapStatus(true)
                        .build();
                post.setScrapCount(post.getScrapCount() + 1);
            } else { // 스크랩 한 적 있는 경우
                if (postScrap.getScrapStatus() == true) { //스크랩한 상태면 취소
                    postScrap.setScrapStatus(false);
                    if (post.getScrapCount() > 0) post.setScrapCount(post.getScrapCount() - 1);
                    else throw new ApplicationException(NOT_FOUND_EXCEPTION);
                } else { //스크랩 안 한 경우
                    postScrap.setScrapStatus(true);
                    post.setScrapCount(post.getScrapCount() + 1);
                }
            }
            postScrapRepository.save(postScrap);
            postRepository.save(post);
        }catch(Exception e){
            throw new ApplicationException(NOT_FOUND_EXCEPTION);
        }
    }

    public List<MyPageRes> getMyPostList(Member member) {
        // Validation

        // Business Logic
        List<Post> postList = postRepository.findAllByMemberAndStatusAndDeletedAtIsNull(member, RECRUITING);

        List<MyPageRes> myPageResList = postList.stream()
                .map(post -> {
                        List<String> categoryList = post.getCategories().stream()
                                .map(category -> category.getCategoryType().toString())
                                .toList();

                        return MyPageRes.of(post, member, categoryList);
                    })
                .collect(Collectors.toList());

        // Return
        return myPageResList;
    }
}