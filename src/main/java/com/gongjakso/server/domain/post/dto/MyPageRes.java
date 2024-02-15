package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.member.dto.MemberRes;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.enumerate.CategoryType;
import com.gongjakso.server.domain.post.enumerate.MeetingMethod;
import com.gongjakso.server.domain.post.enumerate.PostStatus;

import java.time.LocalDateTime;
import java.util.List;

public record MyPageRes(
        Long postId,
        Long memberId,
        Long memberName,
        String title,
        String contents,
        PostStatus status,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime finishDate,
        Long maxPerson,
        MeetingMethod meetingMethod,
        String meetingArea,
        boolean questionMethod,
        String questionLink,
        boolean postType,
        List<CategoryRes> categoryList

) {

    public record CategoryRes(
            CategoryType categoryType
    ) {

        public static CategoryRes of(Category) {
            return null;
        }
    }
    public static MyPageRes of(Post post, Member member) {
        return null;
    }
}
