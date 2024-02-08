package com.gongjakso.server.domain.post.service;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.common.Pagination;
import com.gongjakso.server.domain.post.dto.GetProjectRes;
import com.gongjakso.server.domain.post.dto.PostDeleteRes;
import com.gongjakso.server.domain.post.dto.PostReq;
import com.gongjakso.server.domain.post.dto.PostRes;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gongjakso.server.global.exception.ErrorCode.*;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;


    @Transactional
    public PostRes create(Member member, PostReq req) {
        Post entity = new Post(req.getTitle(), member, req.getContents(), req.getStatus(), req.getStartDate(), req.getEndDate(),
                req.getFinishDate(), req.getMaxPerson(), req.getMeetingMethod(), req.getMeetingArea(),  req.isQuestionMethod(),
                req.getQuestionLink(), req.isPostType());

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
    public PostRes modify(Member member, Long id, PostReq req) {
        Post entity = postRepository.findByPostIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_EXCEPTION));
        if(!member.getMemberId().equals(entity.getMember().getMemberId())){
            throw new ApplicationException(UNAUTHORIZED_EXCEPTION);
        }

        entity.modify(req);

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
    public Page<GetProjectRes> getProjects(Pageable p) throws ApplicationException {
        int page = p.getPageNumber();
        int size = p.getPageSize();
        try {
            Pagination pagination = new Pagination((int) postRepository.count(), page, size);
            System.out.println("Total Count: " + postRepository.count());

            // Pageable 인덱스 조정
            Pageable pageable = PageRequest.of(pagination.getPage(), size);

            return postRepository.findAllProjects(pageable).map(post -> new GetProjectRes(
                    post.getPostId(),
                    post.getTitle(),
                    post.getName(),
                    post.getStatus(),
                    post.getStartDate(),
                    post.getFinishDate()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
    }
    */

    public Page<GetProjectRes> getProjectsBySearchWord(String searchWord, Pageable page) throws ApplicationException {
        try {
            searchWord = searchWord.replaceAll(" ", ""); // 검색어에서 공백 제거

            if (!searchWord.isBlank()) {

                Page<GetProjectRes> posts = postRepository.findBySearchWord(searchWord.toLowerCase(), page);

                //검색 결과가 없는 경우 코드 작성 필요

                return posts.map(post -> new GetProjectRes(
                        post.getPostId(),
                        post.getTitle(),
                        post.getName(),
                        post.getStatus(),
                        post.getStartDate(),
                        post.getFinishDate()
                ));

            } else {
                Pagination pagination = new Pagination((int) postRepository.count(), page.getPageNumber(), page.getPageSize());
                System.out.println("Total Count: " + postRepository.count());

                // Pageable 인덱스 조정
                Pageable pageable = PageRequest.of(pagination.getPage(), page.getPageSize());

                return postRepository.findAllProjects(pageable).map(post -> new GetProjectRes(
                        post.getPostId(),
                        post.getTitle(),
                        post.getName(),
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