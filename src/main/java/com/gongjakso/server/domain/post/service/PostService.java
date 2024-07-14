package com.gongjakso.server.domain.post.service;

import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.apply.repository.ApplyRepository;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.dto.*;
import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.PostScrap;
import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import com.gongjakso.server.domain.post.enumerate.StackNameType;
import com.gongjakso.server.domain.post.projection.ContestProjection;
import com.gongjakso.server.domain.post.projection.ProjectProjection;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.domain.post.repository.PostScrapRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.gongjakso.server.domain.post.enumerate.PostStatus.EXTENSION;
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
        Post entity = Post.builder()
                .title(req.title())
                .member(member)
                .contents(req.contents())
                .contestLink(req.contestLink())
                .startDate(req.startDate())
                .endDate(req.endDate())
                .finishDate(req.finishDate())
                .maxPerson(req.maxPerson())
                .meetingMethod(req.meetingMethod())
                .meetingCity(req.meetingCity())
                .meetingTown(req.meetingTown())
                .questionMethod(req.questionMethod())
                .questionLink(req.questionLink())
                .postType(req.postType())
                .stackNames(new ArrayList<>())
                .categories(new ArrayList<>())
                .build();

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
        Post post = postRepository.findWithStackNameAndCategoryUsingFetchJoinByPostId(postId).orElseThrow(() -> new ApplicationException(NOT_FOUND_POST_EXCEPTION));
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

    /**
     * 공모전/프로젝트 공고 목록 조회 및 페이지네이션 API
     * @param sort 최신순, 인기순(스크랩 수가 높은 순) 정렬
     * @param meetingCity 오프라인 회의일 경우, 만나는 지역 구분 1 (도/광역시/특별시) - 별도의 입력이 없을 경우 전체로 들어옴
     * @param meetingTown 오프라인 회의일 경우, 만나는 지역 구분 2 (시/군/구) - 별도의 입력이 없을 경우 전체로 들어옴
     * @param category 공모전 종류 - 현재는 필터 적용 안 되어 있지만, 추후 추가될 예정
     * @param searchWord 검색어
     * @param page 페이지네이션에 필요한 오프셋, 사이즈 정보를 담은 Pageable 객체
     * @return Page<GetContestRes> 공모전 공고 목록
     */
    @Transactional(readOnly = true)
    public Page<GetContestRes> getContestsByFilter(String sort, String meetingCity, String meetingTown, String category, String searchWord, Pageable page) {
        // Business Logic
        List<PostStatus> statusList = Arrays.asList(RECRUITING, EXTENSION); // 공고가 모집/연장 상태인 경우만 조회되도록 하기 위한 상태값 설정
        String search = (searchWord != null && !searchWord.isEmpty()) ? searchWord.toLowerCase() : searchWord;
        Sort sortCondition = switch (sort) {
            case "createdAt" -> Sort.by("created_at").descending();
            case "scrapCount" -> Sort.by("scrap_count").descending();
            default -> throw new IllegalStateException("Unexpected value: " + sort);
        };

        page = PageRequest.of(page.getPageNumber(), page.getPageSize(), sortCondition);
        Page<Long> postIdPage = postRepository.findContestPaginationByFilter(
                search,
                LocalDateTime.now(),
                statusList,
                meetingCity,
                meetingTown,
                page
        );

        for(Long postId: postIdPage) {
            System.out.println(postId);
        }

        List<ContestProjection> contestProjectionList = switch (sort) {
            case "createdAt" -> postRepository.findContestProjectionListByPostIdListAndCreatedAtDesc(postIdPage.getContent());
            case "scrapCount" -> postRepository.findContestProjectionListByPostIdListAndScrapCountAtDesc(postIdPage.getContent());
            default -> throw new IllegalStateException("Unexpected value: " + sort);
        };

        // PostId 기준으로 그룹화
        Map<Long, List<ContestProjection>> groupedByPostId = contestProjectionList.stream()
                .collect(Collectors.groupingBy(ContestProjection::getPostId, LinkedHashMap::new, Collectors.toList()));

        // PostId 기준으로 그룹화된 맵을 사용하여 GetContestRes 객체 리스트 생성
        List<GetContestRes> contestResList = groupedByPostId.entrySet().stream()
                .map(entry -> {
                    Long postId = entry.getKey();
                    List<ContestProjection> contestProjections = entry.getValue();

                    // CategoryRes 리스트 구성
                    List<CategoryRes> categoryResList = contestProjections.stream()
                            .map(contestProjection -> CategoryRes.builder()
                                    .categoryId(contestProjection.getCategoryId())
                                    .categoryType(contestProjection.getCategoryType())
                                    .size(contestProjection.getCategorySize())
                                    .build())
                            .collect(Collectors.toList());

                    // GetContestRes 객체 생성
                    ContestProjection firstProjection = contestProjections.get(0); // 여기서도 첫 번째 객체를 가져올 수 있습니다.
                    return GetContestRes.builder()
                            .postId(postId)
                            .title(firstProjection.getTitle())
                            .name(firstProjection.getMemberName())
                            .status(firstProjection.getStatus())
                            .startDate(firstProjection.getStartDate())
                            .endDate(firstProjection.getEndDate())
                            .finishDate(firstProjection.getFinishDate())
                            .daysRemaining(firstProjection.getDaysRemaining())
                            .categories(categoryResList)
                            .scrapCount(firstProjection.getScrapCount())
                            .build();
                })
                .collect(Collectors.toList());

        // Response
        return new PageImpl<>(contestResList, postIdPage.getPageable(), postIdPage.getTotalPages());
    }

    /**
     * 프로젝트 공고 목록 조회 및 페이지네이션 API
     * @param sort 최신순, 인기순(스크랩 수가 높은 순) 정렬
     * @param meetingCity 도/광역시/특별시 - 별도의 입력이 없을 경우 전체로 들어옴
     * @param meetingTown 시/군/구 - 별도의 입력이 없을 경우 전체로 들어옴
     * @param stackName 기술 스택
     * @param searchWord 검색어
     * @param page 페이지네이션에 필요한 오프셋, 사이즈 정보를 담은 Pageable 객체
     * @return Page<GetProjectRes> 프로젝트 공고 목록
     */
    public Page<GetProjectRes> getProjectsByFilter(String sort, String meetingCity, String meetingTown, String stackName, String searchWord, Pageable page) {
        // Validation
        if(stackName != null && (stackName.isBlank() || StackNameType.isValid(stackName))) {
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }

        // Business Logic
        List<PostStatus> statusList = Arrays.asList(RECRUITING, EXTENSION); // 공고가 모집/연장 상태인 경우만 조회되도록 하기 위한 상태값 설정
        String search = (searchWord != null && !searchWord.isEmpty()) ? searchWord.toLowerCase() : searchWord;
        Page<ProjectProjection> projectProjectionPage = switch (sort) {
            case "createdAt":
                page = PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by("created_at").descending());
                yield postRepository.findProjectPaginationByFilter(
                        search,
                        LocalDateTime.now(), 
                        statusList, 
                        meetingCity,
                        meetingTown,
                        stackName,
                        page
                );
            case "scrapCount":
                page = PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by("scrap_count").descending());
                yield postRepository.findProjectPaginationByFilter(
                        search,
                        LocalDateTime.now(), 
                        statusList,
                        meetingCity, 
                        meetingTown,
                        stackName,
                        page
                );
            default:
                throw new IllegalStateException("Unexpected value: " + sort);
        };

        // Response
        return new PageImpl<>(null, projectProjectionPage.getPageable(), projectProjectionPage.getTotalPages());
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
        return PostScrapRes.of(postScrap, post.getScrapCount());
    }

    @Transactional
    public PostScrapRes scrapGet(Member member, Long postId){
        Post post = postRepository.findByPostIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST_EXCEPTION));
        if (member.getMemberId() == null) {
            throw new ApplicationException(UNAUTHORIZED_EXCEPTION);
        }
        if(postScrapRepository.findByPostAndMember(post, member)==null){ //post, member 정보는 존재하되, scrap한적이 없는 경우 default false값 반환
            return new PostScrapRes(post.getPostId(), member.getMemberId(), false, post.getScrapCount());
        }
        PostScrap postScrap = postScrapRepository.findByPostAndMember(post, member);
        return PostScrapRes.of(postScrap, post.getScrapCount());
    }

    @Transactional
    public List<MyPageRes> getMyPostList(Member member) {
        // Validation

        // Business Logic
        List<PostStatus> statusList = Arrays.asList(RECRUITING, EXTENSION);
        List<Post> postList = postRepository.findAllByMemberAndStatusInAndDeletedAtIsNullOrderByCreatedAtDesc(member, statusList);

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
            if(applyRepository.existsApplyByMemberAndPostAndIsCanceledIsFalse(member, post)) {
             role = "APPLICANT";
            }
        }

        // Return
        return GetPostRelation.of(role);
    }

    @Transactional
    public Page<GetProjectRes> getMyScrapProject(Member member, Pageable page){
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize());

        Page<PostScrap> scrapPageList = postScrapRepository.findAllByMemberAndPostPostTypeTrueAndPostDeletedAtIsNullAndScrapStatusTrueOrderByPostScrapIdDesc(member, pageable);

        List<GetProjectRes> myScrapProjects = scrapPageList.stream()
                .map(scrap -> {
                    Post post = scrap.getPost();
                    post.getCategories().size();
                    post.getStackNames().size();
                    return GetProjectRes.of(post);
                })
                .collect(Collectors.toList()); // 리스트로 수집

        // 필터링된 리스트를 페이지로 반환
        return new PageImpl<>( myScrapProjects, pageable, myScrapProjects.size());
    }

    @Transactional
    public Page<GetContestRes> getMyScrapContest(Member member, Pageable page){
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize());

        Page<PostScrap> scrapPageList = postScrapRepository.findAllByMemberAndPostPostTypeFalseAndPostDeletedAtIsNullAndScrapStatusTrueOrderByPostScrapIdDesc(member, pageable);

        List<GetContestRes> myScrapContests = scrapPageList.stream()
                .map(scrap -> {
                    Post post = scrap.getPost();
                    post.getCategories().size();
                    post.getStackNames().size();
                    return GetContestRes.of(post);
                })
                .collect(Collectors.toList()); // 리스트로 수집

        // 필터링된 리스트를 페이지로 반환
        return new PageImpl<>(myScrapContests, pageable, myScrapContests.size());
    }


    @Transactional
    public PostSimpleRes completePost(Member member, Long postId) {
        // Validation: Post 논리적 삭제 및 사용자의 권한 여부 확인
        Post post = postRepository.findByPostIdAndDeletedAtIsNull(postId).orElseThrow(() -> new ApplicationException(NOT_FOUND_POST_EXCEPTION));
        if(!post.getMember().getMemberId().equals(member.getMemberId())){
            throw new ApplicationException(UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic
        post.updateStatus(PostStatus.COMPLETE);
        Post savePost = postRepository.save(post);

        // Response
        return PostSimpleRes.of(savePost);
    }
}