package com.gongjakso.server.domain.post.service;

import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.apply.repository.ApplyRepository;
import com.gongjakso.server.domain.member.entity.Member;
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
import com.gongjakso.server.global.exception.ErrorCode;
import com.gongjakso.server.global.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.gongjakso.server.domain.post.enumerate.PostStatus.RECRUITING;
import static com.gongjakso.server.global.exception.ErrorCode.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostScrapRepository postScrapRepository;
    private final ApplyRepository applyRepository;

    @Transactional
    public PostRes create(Member member, PostReq req) {
        // Validation - 공고/프로젝트별 1개의 모집 공고만 활성화되어 있는지 유효성 검사
        if (!req.postType() && postRepository.countByMemberAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatus(member, LocalDateTime.now(), RECRUITING) > 0) { //공모전 공고 모집 개수 제한
            throw new ApplicationException(NOT_POST_EXCEPTION);
        }
        if (req.postType()  && postRepository.countByMemberAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatus(member, LocalDateTime.now(), RECRUITING) > 0) { //프로젝트 공고 모집 개수 제한
            throw new ApplicationException(NOT_POST_EXCEPTION);
        }
        if (req.maxPerson() != req.categories().stream().mapToInt(Category::getSize).sum()) {
            throw new ApplicationException(ILLEGAL_POST_EXCEPTION);
        }

        // Business Logic
        // TODO: new 방식으로 서비스에서 생성하는 것이 아닌, DTO 내의 메소드를 활용하도록 변경하는 것은 어떤지 고려할 필요 존재
        Post entity = new Post(req.title(), member, req.contents(), req.contestLink(), req.startDate(), req.endDate(),
                req.finishDate(), req.maxPerson(), req.meetingMethod(), req.meetingCity(), req.meetingTown(), req.questionMethod(),
                req.questionLink(), req.postType(), new ArrayList<>(), new ArrayList<>());

        List<StackName> stackNames = req.stackNames().stream()
                .map(stackNameReq -> new StackName(entity, stackNameReq.getStackNameType()))
                .toList();
        entity.getStackNames().addAll(stackNames);

        List<Category> categories = req.categories().stream()
                .map(categoryReq -> new Category(entity, categoryReq.getCategoryType().toString(), categoryReq.getSize()))
                .toList();
        entity.getCategories().addAll(categories);
        Post savePost = postRepository.save(entity);

        // Response
        return PostRes.of(savePost);
    }

    @Transactional
    public Optional<?> read(PrincipalDetails principalDetails, Long postId) {
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(postId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_POST_EXCEPTION));
        int current_person = (int) applyRepository.countApplyWithStackNameUsingFetchJoinByPostAndApplyType(post, ApplyType.PASS);
      
        post.updatePostView(post.getPostView());

        post.getCategories().size();
        post.getStackNames().size();

        if(principalDetails == null) {
            return Optional.of(PostDetailRes.of(post, current_person,null));
        }else{
            return Optional.of(PostDetailRes.of(post, current_person, principalDetails.getMember().getMemberId()));
        }
    }


    @Transactional
    public PostRes modify(Member member, Long id, PostModifyReq req) {
        // Validation - 공고 존재 여부 및 공고 게시자 여부 확인
        Post entity = postRepository.findByPostIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST_EXCEPTION));
        if(!member.getMemberId().equals(entity.getMember().getMemberId())){
            throw new ApplicationException(UNAUTHORIZED_EXCEPTION);
        }
        if (req.maxPerson() != req.categories().stream().mapToInt(Category::getSize).sum()) {
            throw new ApplicationException(ILLEGAL_POST_EXCEPTION);
        }

        // Business Logic
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

        // Response
        return PostRes.of(entity);
    }

    @Transactional
    public PostDeleteRes delete(Member member, Long id) {
        Post entity = postRepository.findByPostIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST_EXCEPTION));

        if(!member.getMemberId().equals(entity.getMember().getMemberId())){
            throw new ApplicationException(UNAUTHORIZED_EXCEPTION);
        }

        postRepository.delete(entity);
        return PostDeleteRes.of(entity, member);
    }

    /*
    전체 공모전 공고 목록 조회
     */
    @Transactional
    public Page<GetContestRes> getContests(String sort, Pageable page) throws ApplicationException {
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize());
        Page<Post> posts;
        if(sort.equals("createdAt")){ //최신순
            posts = postRepository.findAllByPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByPostIdDesc(LocalDateTime.now(), RECRUITING, pageable);
        } else{ //스크랩순
            posts = postRepository.findAllByPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescPostIdDesc(LocalDateTime.now(), RECRUITING, pageable);
        }

        posts.forEach(post -> post.getCategories().size());
        posts.forEach(post -> post.getStackNames().size());
        return posts.map(post -> GetContestRes.of(post));
    }

    /*
   검색어 기반 공모전 공고 목록 조회
    */
    @Transactional
    public Page<GetContestRes> getContestsBySearchWord(String sort, String searchWord, Pageable page) throws ApplicationException {
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize());
        Page<Post> posts;
        if (sort.equals("createdAt")) {
            posts = postRepository.findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByPostIdDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, pageable);
        } else{
            posts = postRepository.findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescPostIdDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, pageable);
        }

        posts.forEach(post -> post.getCategories().size());
        posts.forEach(post -> post.getStackNames().size());
        return posts.map(post -> GetContestRes.of(post));
    }

    /*
    지역, 카테고리 기반 공모전 공고 목록 조회
     */
    @Transactional
    public Page<GetContestRes> getContestsByMeetingAreaAndCategoryAndSearchWord(
            String sort, String meetingCity, String meetingTown, String category, String searchWord, Pageable page) throws ApplicationException {
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize());
        if(meetingTown.equals("전체")){
            meetingTown = "";
        }
        if(!category.isBlank()) {
            if (!CategoryType.isValid(category)){
                throw new ApplicationException(INVALID_VALUE_EXCEPTION);
            }
            Page<Post> posts;
            if (sort.equals("createdAt")) {
                posts = postRepository.findAllPostsJoinedWithCategoriesByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsAndCategoriesCategoryTypeContainsOrderByPostIdDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingCity, meetingTown, category.toString(), pageable);
            }else{
                posts = postRepository.findAllPostsJoinedWithCategoriesByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsAndCategoriesCategoryTypeContainsOrderByScrapCountDescPostIdDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingCity, meetingTown, category.toString(), pageable);
            }
            posts.forEach(post -> post.getCategories().size());
            posts.forEach(post -> post.getStackNames().size());
            return posts.map(post -> GetContestRes.of(post));
        } else{
            Page<Post> posts;
            if (sort.equals("createdAt")) {
                posts = postRepository.findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsOrderByPostIdDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingCity, meetingTown, pageable);
            }else{
                posts = postRepository.findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsOrderByScrapCountDescPostIdDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingCity, meetingTown, pageable);
            }
            posts.forEach(post -> post.getCategories().size());
            posts.forEach(post -> post.getStackNames().size());
            return posts.map(post -> GetContestRes.of(post));
        }
    }

    /*
    전체 프로젝트 공고 목록 조회
     */
    @Transactional
    public Page<GetProjectRes> getProjects(String sort, Pageable page) throws ApplicationException {
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize());
        Page<Post> posts;
        if(sort.equals("createdAt")){ //최신순
            posts = postRepository.findAllByPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByPostIdDesc(LocalDateTime.now(), RECRUITING, pageable);
        } else{ //스크랩순
            posts = postRepository.findAllByPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescPostIdDesc(LocalDateTime.now(), RECRUITING, pageable);
        }
        posts.forEach(post -> post.getCategories().size());
        posts.forEach(post -> post.getStackNames().size());
        return posts.map(post -> GetProjectRes.of(post));
    }

    /*
    검색어 기반 프로젝트 공고 목록 조회
     */
    @Transactional
    public Page<GetProjectRes> getProjectsBySearchWord(String sort, String searchWord, Pageable page) throws ApplicationException {
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize());
        Page<Post> posts;
        if (sort.equals("createdAt")) {
            posts = postRepository.findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByPostIdDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, pageable);
        } else{
            posts = postRepository.findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescPostIdDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, pageable);
        }
        posts.forEach(post -> post.getCategories().size());
        posts.forEach(post -> post.getStackNames().size());
        return posts.map(post -> GetProjectRes.of(post));
    }

    /*
    지역, 스택 기반 프로젝트 공고 목록 조회
     */
    @Transactional
    public Page<GetProjectRes> getProjectsByMeetingAreaAndStackNameAndSearchWord(
            String sort, String meetingCity, String meetingTown, String stackName, String searchWord, Pageable page) throws ApplicationException {
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize());
        if(meetingTown.equals("전체")){
            meetingTown = "";
        }
        if(meetingCity.equals("전체")){
            meetingCity = "";
        }
        if(!stackName.isBlank()) {
            if (!StackNameType.isValid(stackName)){
                throw new ApplicationException(INVALID_VALUE_EXCEPTION);
            }
            Page<Post> posts;
            if (sort.equals("createdAt")) {
                posts = postRepository.findAllPostsJoinedWithStackNamesByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsAndStackNamesStackNameTypeContainsOrderByPostIdDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingCity,meetingTown, stackName.toString(), pageable);
            }else{
                posts = postRepository.findAllPostsJoinedWithStackNamesByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsAndStackNamesStackNameTypeContainsOrderByScrapCountDescPostIdDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingCity,meetingTown, stackName.toString(), pageable);
            }
            posts.forEach(post -> post.getCategories().size());
            posts.forEach(post -> post.getStackNames().size());
            return posts.map(post -> GetProjectRes.of(post));
        } else{
            Page<Post> posts;
            if (sort.equals("createdAt")) {
                posts = postRepository.findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsOrderByPostIdDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingCity,meetingTown, pageable);
            }else{
                posts = postRepository.findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsOrderByScrapCountDescPostIdDesc(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingCity,meetingTown, pageable);
            }
            posts.forEach(post -> post.getCategories().size());
            posts.forEach(post -> post.getStackNames().size());
            return posts.map(GetProjectRes::of);
        }
    }

    /*
        공고 스크랩 기능
     */
    @Transactional
    public PostScrapRes scrapPost(Member member, Long postId) {
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
            if (postScrap.getScrapStatus()) { //스크랩한 상태면 취소
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
    }

    @Transactional
    public PostScrapRes scrapGet(Member member, Long postId){
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
    }

    @Transactional
    public List<MyPageRes> getMyPostList(Member member) {
        // Validation

        // Business Logic
        List<Post> postList = postRepository.findAllByMemberAndStatusAndDeletedAtIsNull(member, RECRUITING);

        // Return
        return postList.stream()
                .map(post -> {
                    List<String> categoryList = post.getCategories().stream()
                            .map(category -> category.getCategoryType().toString())
                            .toList();

                    return MyPageRes.of(post, member, categoryList);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public GetPostRelation checkPostRelation(Member member, Long postId) {
        // Validation
        Post post = postRepository.findByPostIdAndDeletedAtIsNull(postId).orElseThrow(() -> new ApplicationException(ALREADY_DELETE_EXCEPTION));

        // Business Logic
        String role = "GENERAL";
        if(post.getMember().getMemberId().equals(member.getMemberId())){
            role = "LEADER";
        }
        else {
            if(applyRepository.existsApplyByMemberAndPost(member, post)) {
             role = "APPLICANT";
            }
        }

        // Return
        return GetPostRelation.of(role);
    }

    @Transactional
    public Page<GetProjectRes> getMyScrapProject(Member member, Pageable page){
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize());

        Page<PostScrap> scrapPageList = postScrapRepository.findAllByMemberAndScrapStatusTrueOrderByPostScrapIdDesc(member, pageable);

        List<GetProjectRes> filteredProjects = scrapPageList.stream()
                .filter(scrap -> {
                    Post post = scrap.getPost();

                    //유효한 post만 남기기
                    return post != null &&
                            post.isPostType() == true &&
                            post.getDeletedAt() == null;
                })
                .map(scrap -> {
                    Post post = scrap.getPost();
                    post.getCategories().size();
                    post.getStackNames().size();
                    return GetProjectRes.of(post);
                })
                .collect(Collectors.toList()); // 리스트로 수집

        // 필터링된 리스트를 페이지로 반환
        return new PageImpl<>(filteredProjects, pageable, scrapPageList.getTotalElements());
    }

    @Transactional
    public Page<GetContestRes> getMyScrapContest(Member member, Pageable page){
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize());

        Page<PostScrap> scrapPageList = postScrapRepository.findAllByMemberAndScrapStatusTrueOrderByPostScrapIdDesc(member, pageable);

        List<GetContestRes> filteredContests = scrapPageList.stream()
                .filter(scrap -> {
                    Post post = scrap.getPost();

                    //유효한 post만 남기기
                    return post != null &&
                            post.isPostType() == false &&
                            post.getDeletedAt() == null;
                })
                .map(scrap -> {
                    Post post = scrap.getPost();
                    post.getCategories().size();
                    post.getStackNames().size();
                    return GetContestRes.of(post);
                })
                .collect(Collectors.toList()); // 리스트로 수집

        // 필터링된 리스트를 페이지로 반환
        return new PageImpl<>(filteredContests, pageable, scrapPageList.getTotalElements());
    }
}