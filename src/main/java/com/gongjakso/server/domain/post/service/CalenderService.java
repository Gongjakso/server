package com.gongjakso.server.domain.post.service;

import com.gongjakso.server.domain.apply.dto.ApplyList;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.dto.CalenderRes;
import com.gongjakso.server.domain.post.dto.ScrapPost;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.PostScrap;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.domain.post.repository.PostScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CalenderService {
    private final PostScrapRepository postScrapRepository;
    private final PostRepository postRepository;
    public CalenderRes findScrapPost(Member member,int year,int month){
        List<PostScrap> postScraps = postScrapRepository.findByMemberAndScrapStatus(member,true);
        List<Post> posts = postScraps.stream()
                .map(postScrap -> {
                    Post post = postRepository.findByPostId(postScrap.getPost().getPostId());
                    LocalDateTime endDate = post.getEndDate();
                    if (endDate.getYear() == year && endDate.getMonthValue() == month) {
                        return post;
                    } else {
                        return null;
                    }

                })
                .filter(post->post!=null)
                .toList();
        List<ScrapPost> scrapPosts = posts.stream().map(ScrapPost::of)
                .collect(Collectors.toList());
        return CalenderRes.of(scrapPosts);
    }
}
