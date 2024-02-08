package com.gongjakso.server.domain.post.service;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.repository.MemberRepository;
import com.gongjakso.server.domain.post.dto.PostReq;
import com.gongjakso.server.domain.post.dto.PostRes;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.repository.PostRepository;
import io.github.classgraph.PackageInfo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PostRes create(Member member, PostReq req) {
//        validateMemberId(memberId);
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
                .maxPerson(post.getMaxPerson())
                .meetingMethod(post.getMeetingMethod())
                .meetingArea(post.getMeetingArea())
                .questionMethod(post.isQuestionMethod())
                .questionLink(post.getQuestionLink())
                .isProject(post.isProject())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .deletedAt(post.getDeletedAt())
                .build();
    }

    @Transactional
    public PostRes read(Long id, PostReq req) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        post.read(req);

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
                .isProject(post.isProject())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .deletedAt(post.getDeletedAt())
                .build();
    }

    @Transactional
    public PostRes modify(Long id, PostReq req) {
        Post entity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        entity.modify(req);

        return PostRes.builder()
                .postId(entity.getPostId())
                .memberId(entity.getMember().getMemberId())
                .title(entity.getTitle())
                .contents(entity.getContents())
                .status(entity.getStatus())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .maxPerson(entity.getMaxPerson())
                .meetingMethod(entity.getMeetingMethod())
                .meetingArea(entity.getMeetingArea())
                .questionMethod(entity.isQuestionMethod())
                .questionLink(entity.getQuestionLink())
                .isProject(entity.isProject())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

//    // 멤버 유효성 판단
//    private void validateMemberId(Member member) {
//        Optional<Member> optionalMember = memberRepository.findMemberById(member.getMemberId());
//        if (optionalMember.isEmpty()) {
//            throw new IllegalArgumentException("존재하지 않는 멤버 ID입니다.");
//        }
//    }
}