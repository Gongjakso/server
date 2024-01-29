package com.gongjakso.server.domain.post.service;

import com.gongjakso.server.domain.post.dto.PostReq;
import com.gongjakso.server.domain.post.dto.PostRes;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public PostRes modify(Long id, PostReq req) {
        Post entity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        entity.modify(req);
        entity.setModifiedAt(LocalDateTime.now());
        Post modifiedPost = postRepository.save(entity);

        return PostRes.builder()
                .postId(modifiedPost.getPostId())
                .memberId(modifiedPost.getMember().getMemberId())
                .title(modifiedPost.getTitle())
                .contents(modifiedPost.getContents())
                .status(modifiedPost.getStatus())
                .startDate(modifiedPost.getStartDate())
                .endDate(modifiedPost.getEndDate())
                .maxPerson(modifiedPost.getMaxPerson())
                .meetingMethod(modifiedPost.getMeetingMethod())
                .meetingArea(modifiedPost.getMeetingArea())
                .questionMethod(modifiedPost.isQuestionMethod())
                .questionLink(modifiedPost.getQuestionLink())
                .isProject(modifiedPost.isProject())
                .createdAt(modifiedPost.getCreatedAt())
                .modifiedAt(modifiedPost.getModifiedAt())
                .deletedAt(modifiedPost.getDeletedAt())
                .build();

    }
}