package com.gongjakso.server.domain.post.service;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.dto.CalendarRes;
import com.gongjakso.server.domain.post.dto.ScrapPost;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.PostScrap;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.domain.post.repository.PostScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService {
    private final PostScrapRepository postScrapRepository;
    private final PostRepository postRepository;
    public CalendarRes findScrapPost(Member member, int year, int month){
        List<PostScrap> postScraps = postScrapRepository.findByMemberAndScrapStatus(member,true);
        List<Long> postIdList = postScraps.stream().map(postScrap -> postScrap.getPost().getPostId()).toList();
        List<Post> posts = postRepository.findAllByEndDateBetweenAndPostIdIn(getFirstDayOfMonth(year,month),getLastDayOfMonth(year,month), postIdList);
        List<ScrapPost> scrapPosts = posts.stream().map(ScrapPost::of)
                .collect(Collectors.toList());
        return CalendarRes.of(scrapPosts);
    }
    public static LocalDateTime getFirstDayOfMonth(int year, int month) {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        return LocalDateTime.of(firstDayOfMonth, LocalTime.MIN);
    }

    public static LocalDateTime getLastDayOfMonth(int year, int month) {
        LocalDate lastDayOfMonth = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
        return LocalDateTime.of(lastDayOfMonth, LocalTime.MAX);
    }
}
