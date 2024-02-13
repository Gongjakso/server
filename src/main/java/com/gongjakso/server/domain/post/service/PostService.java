package com.gongjakso.server.domain.post.service;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.common.Pagination;
import com.gongjakso.server.domain.post.dto.GetProjectRes;
import com.gongjakso.server.domain.post.dto.PostDeleteRes;
import com.gongjakso.server.domain.post.dto.PostReq;
import com.gongjakso.server.domain.post.dto.PostRes;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.domain.post.enumerate.StackNameType;
import com.gongjakso.server.domain.post.repository.PostRepository;
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


    @Transactional
    public PostRes create(Member member, PostReq req) {
        Post entity = new Post(req.getTitle(), member, req.getContents(), req.getStatus(), req.getStartDate(), req.getEndDate(),
                req.getFinishDate(), req.getMaxPerson(), req.getMeetingMethod(), req.getMeetingArea(), req.isQuestionMethod(),
                req.getQuestionLink(), req.isPostType(), new ArrayList<>());

        List<StackName> stackNames = req.getStackNames().stream()
                .map(stackNameType -> new StackName(entity, stackNameType.getStackNameType().toString()))
                .collect(Collectors.toList());
        entity.getStackNames().addAll(stackNames);

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

        if(!member.getMemberId().equals(post.getMember().getMemberId())) {
            throw new ApplicationException(UNAUTHORIZED_EXCEPTION);
        }

        return PostRes.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .title(post.getTitle())
                .contents(post.getContents())
                .status(post.getStatus())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .maxPerson(post.getMaxPerson())
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

        List<StackName> updatedStackNames = req.getStackNames().stream()
                .map(stackNameType -> new StackName(entity, stackNameType.getStackNameType().toString()))
                .collect(Collectors.toList());
        entity.getStackNames().addAll(updatedStackNames);

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

        postRepository.delete(entity);
        return PostDeleteRes.builder()
                .postId(entity.getPostId())
                .memberId(entity.getMember().getMemberId())
                .build();
    }

    /*
    전체 프로젝트 공고 목록 조회
     */
    public Page<GetProjectRes> getProjects(Pageable p) throws ApplicationException {
        int page = p.getPageNumber();
        int size = p.getPageSize();
        try {
            Pagination pagination = new Pagination((int) postRepository.count(), page, size);
            Pageable pageable = PageRequest.of(pagination.getPage(), size);

            return postRepository.findAllByPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatus(LocalDateTime.now(), RECRUITING, pageable).map(post -> new GetProjectRes(
                    post.getPostId(),
                    post.getTitle(),
                    post.getMember().getName(),
                    post.getStatus(),
                    post.getStartDate(),
                    post.getFinishDate()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
    }

    /*
    검색어 기반 프로젝트 공고 목록 조회
     */
    public Page<GetProjectRes> getProjectsBySearchWord(String searchWord, Pageable page) throws ApplicationException {
        try {
            Pagination pagination = new Pagination((int) postRepository.count(), page.getPageNumber(), page.getPageSize());
            Pageable pageable = PageRequest.of(pagination.getPage(), page.getPageSize());
            searchWord = searchWord.replaceAll(" ", ""); // 검색어에서 공백 제거
            Page<Post> posts = postRepository.findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatus(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, pageable);
            return posts.map(post -> new GetProjectRes(
                    post.getPostId(),
                    post.getTitle(),
                    post.getMember().getName(),
                    post.getStatus(),
                    post.getStartDate(),
                    post.getFinishDate()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
    }

    /*
    지역, 스택 기반 프로젝트 공고 목록 조회
     */
    public Page<GetProjectRes> getProjectsByMeetingAreaAndStackNameAndSearchWord(
            String meetingArea, StackNameType stackName, String searchWord, Pageable page) throws ApplicationException {
        try {
            Pagination pagination = new Pagination((int) postRepository.count(), page.getPageNumber(), page.getPageSize());
            Pageable pageable = PageRequest.of(pagination.getPage(), page.getPageSize());
            searchWord = searchWord.replaceAll(" ", "");
            if(stackName != null) {
                Page<Post> posts = postRepository.findAllPostsJoinedWithStackNamesByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsAndStackNamesStackNameTypeContains(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingArea, stackName.toString(), pageable);
                return posts.map(post -> new GetProjectRes(
                        post.getPostId(),
                        post.getTitle(),
                        post.getMember().getName(),
                        post.getStatus(),
                        post.getStartDate(),
                        post.getFinishDate()
                ));
            }else{
                Page<Post> posts = postRepository.findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContains(searchWord.toLowerCase(), LocalDateTime.now(), RECRUITING, meetingArea, pageable);
                return posts.map(post -> new GetProjectRes(
                        post.getPostId(),
                        post.getTitle(),
                        post.getMember().getName(),
                        post.getStatus(),
                        post.getStartDate(),
                        post.getFinishDate()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
    }
}