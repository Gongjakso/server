package com.gongjakso.server.domain.post.service;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.repository.MemberRepository;
import com.gongjakso.server.domain.post.dto.PostDeleteRes;
import com.gongjakso.server.domain.post.dto.PostReq;
import com.gongjakso.server.domain.post.dto.PostRes;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import io.github.classgraph.PackageInfo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gongjakso.server.global.exception.ErrorCode.NOT_FOUND_EXCEPTION;
import static com.gongjakso.server.global.exception.ErrorCode.UNAUTHORIZED_EXCEPTION;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostRes create(Member member, PostReq req) {
        Post post = new Post(member, req);
        postRepository.save(post);

        return PostRes.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .title(post.getTitle())
                .contents(post.getContents())
                .status(post.getStatus())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .finishDate(post.getFinishDate())
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
    public Page<GetProjectRes> getProjects(Pageable page) throws ApplicationException {
        try {
            return postRepository.findAll(page).map(projects -> new GetProjectRes(
                    projects.getPostId(),
                    projects.getTitle(),
                    projects.getMeetingArea());
        } catch (Exception e) {
            throw new ApplicationException(INVALID_VALUE_EXCEPTION);
        }
    }
    */
}