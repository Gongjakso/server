package com.gongjakso.server.domain.contest.service;

import com.gongjakso.server.domain.contest.repository.ContestRepository;
import com.gongjakso.server.global.util.redis.RedisClient;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViewService {
    private static final String VIEW_KEY = "contest:view:";

    private final RedisClient redisClient;
    private final ContestRepository contestRepository;

    // 조회수 체크 ( 로그인 ) - redis
    public boolean checkView_Member(long id, long memberId){
        String key = VIEW_KEY+id+":"+memberId;
        String isViewed = redisClient.getValue(key);

        if(isViewed.isEmpty()){
            redisClient.setValue(key,"true", 60 * 24L);
            incrementView(id);
            return true;
        }
        return false;
    }

    // 조회수 체크 ( 비로그인 ) - cookie
    public boolean checkView_Guest(long id, HttpServletRequest request, HttpServletResponse response){
        String cookieName = "contest_"+id;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return false;
                }
            }
        }

        Cookie newCookie = new Cookie(cookieName, "true");
        newCookie.setMaxAge(60 * 60 * 24); // 1일 동안 유지
        newCookie.setPath("/");
        response.addCookie(newCookie);

        incrementView(id);
        return true;
    }

    // Redis에서 조회수 증가
    private void incrementView(Long id) {
        String redisKey = VIEW_KEY + id;
        redisClient.incrementValue(redisKey);  // Redis의 INCR 명령 사용
    }

    // 조회수 저장
    public void saveView() {
//        for (Long id : contestRepository.findAllPostIds()) {
//            String redisKey = VIEW_KEY + id;
//            String viewCountStr = redisClient.getValue(redisKey);
//
//            if (!viewCountStr.isEmpty()) {
//                int redisViewCount = Integer.parseInt(viewCountStr);
//                Post post = postRepository.findById(id).orElseThrow();
//                post.setViewCount(post.getViewCount() + redisViewCount);
//                postRepository.save(post);
//
//                // Redis에서 값 초기화
//                redisClient.deleteValue(redisKey);
//            }
//        }
    }
}
