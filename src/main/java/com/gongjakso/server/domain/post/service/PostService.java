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
        try {
            if (!req.postType() && postRepository.countByMemberAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatus(member, LocalDateTime.now(), RECRUITING) > 0) { //공모전 공고 모집 개수 제한
                throw new ApplicationException(NOT_POST_EXCEPTION);
            }
            if (req.postType()  && postRepository.countByMemberAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatus(member, LocalDateTime.now(), RECRUITING) > 0) { //프로젝트 공고 모집 개수 제한
                throw new ApplicationException(NOT_POST_EXCEPTION);
            }

            Post entity = new Post(req.title(), member, req.contents(), req.contestLink(), req.startDate(), req.endDate(),
                    req.finishDate(), req.maxPerson(), req.meetingMethod(), req.meetingArea(), req.questionMethod(),
                    req.questionLink(), req.postType(), new ArrayList<>(), new ArrayList<>());

            List<StackName> stackNames = req.stackNames().stream()
                    .map(stackNameReq -> new StackName(entity, stackNameReq.getStackNameType()))
                    .toList();
            entity.getStackNames().addAll(stackNames);

            List<Category> categories = req.categories().stream()
                    .map(categoryReq -> new Category(entity, categoryReq.getCategoryType().toString(), categoryReq.getSize()))
                    .toList();
            entity.getCategories().addAll(categories);

            postRepository.save(entity);
            return PostRes.of(entity);
        } catch (ApplicationException e) {
            throw new ApplicationException(NOT_POST_EXCEPTION);
        } catch (Exception e){
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
    }

    @Transactional
    public PostDetailRes read(Member member, Long id) {
        try {
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST_EXCEPTION));
            return PostDetailRes.of(post, member, 3L);
        }catch (Exception e){
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
    }

    @Transactional
    public PostRes modify(Member member, Long id, PostModifyReq req) {
        try{
            Post entity = postRepository.findByPostIdAndDeletedAtIsNull(id)
                    .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST_EXCEPTION));
            if(!member.getMemberId().equals(entity.getMember().getMemberId())){
                throw new ApplicationException(UNAUTHORIZED_EXCEPTION);
            }
            entity.modify(req);

            entity.getStackNames().clear();
            entity.getCategories().clear();
            List<StackName> updatedStackNames = req.stackNames().stream()
                    .map(stackNameReq ->  new StackName(entity, stackNameReq.getStackNameType()))
                    .toList();
            entity.getStackNames().addAll(updatedStackNames);

            List<Category> categories = req.categories().stream()
                    .map(categoryReq ->  new Category(entity, categoryReq.getCategoryType().toString(), categoryReq.getSize()))
                    .toList();
            entity.getCategories().addAll(categories);


            return PostRes.of(entity);
        } catch (Exception e) {
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
    }

    @Transactional
    public PostDeleteRes delete(Member member, Long id) {
        try{
            Post entity = postRepository.findByPostIdAndDeletedAtIsNull(id)
                    .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST_EXCEPTION));

            if(!member.getMemberId().equals(entity.getMember().getMemberId())){
                throw new ApplicationException(UNAUTHORIZED_EXCEPTION);
            }

            postRepository.delete(entity);
            return PostDeleteRes.of(entity, member);
        } catch (Exception e) {
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
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
            return posts.map(post -> GetContestRes.of(post));
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
            return posts.map(post -> GetContestRes.of(post));
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
                return posts.map(post -> GetContestRes.of(post));
            } else{
                Page<Post> posts;
                if (sort.equals("createdAt,desc")) {
                    posts = postRepository.findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsOrderByCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingArea, pageable);
                }else{
                    posts = postRepository.findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsOrderByScrapCountDescCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingArea, pageable);
                }
                return posts.map(post -> GetContestRes.of(post));
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
            return posts.map(post -> GetProjectRes.of(post));
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
            return posts.map(post -> GetProjectRes.of(post));
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
                return posts.map(post -> GetProjectRes.of(post));
            } else{
                Page<Post> posts;
                if (sort.equals("createdAt,desc")) {
                    posts = postRepository.findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsOrderByCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingArea, pageable);
                }else{
                    posts = postRepository.findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsOrderByScrapCountDescCreatedAtDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingArea, pageable);
                }
                return posts.map(post -> GetProjectRes.of(post));
            }
        } catch (Exception e) {
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
    }

    /*
        공고 스크랩 기능
     */
    @Transactional
    public PostScrapRes scrapPost(Member member, Long postId) {
        try {
            Post post = postRepository.findByPostIdAndDeletedAtIsNull(postId)
                    .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST_EXCEPTION));
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
                    else throw new ApplicationException(INVALID_VALUE_EXCEPTION);
                } else { //스크랩 안 한 경우
                    postScrap.setScrapStatus(true);
                    post.setScrapCount(post.getScrapCount() + 1);
                }
            }
            postScrapRepository.save(postScrap);
            postRepository.save(post);
            return new PostScrapRes(postScrap.getPost().getPostId(), postScrap.getMember().getMemberId(), postScrap.getScrapStatus());
        }catch(Exception e){
            throw new ApplicationException(NOT_FOUNT_SCRAP_EXCEPTION);
        }
    }

    public PostScrapRes scrapGet(Member member, Long postId){
        try {
            Post post = postRepository.findByPostIdAndDeletedAtIsNull(postId)
                    .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST_EXCEPTION));
            if (member.getMemberId() == null) {
                throw new ApplicationException(UNAUTHORIZED_EXCEPTION);
            }
            if(postScrapRepository.findByPostAndMember(post, member)==null){ //post, member 정보는 존재하되, scrap한적이 없는 경우 default false값 반환
                return new PostScrapRes(post.getPostId(), member.getMemberId(), false);
            }
            PostScrap postScrap = postScrapRepository.findByPostAndMember(post, member);
            return new PostScrapRes(postScrap.getPost().getPostId(), postScrap.getMember().getMemberId(), postScrap.getScrapStatus());
        }catch(Exception e) {
            throw new ApplicationException(NOT_FOUNT_SCRAP_EXCEPTION);
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