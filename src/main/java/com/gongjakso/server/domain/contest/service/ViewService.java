package com.gongjakso.server.domain.contest.service;

import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.contest.repository.ContestRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import com.gongjakso.server.global.util.redis.RedisClient;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ViewService {
    private static final String CONTEST_VIEW_KEY = "contest:view:";
    private static final String CLIENT_VIEW_KEY = "contest:client:view:";
    private static final String COOKIE_NAME = "contest:cookie:uuid";

    private final RedisClient redisClient;
    private final ContestRepository contestRepository;

    @Cacheable(cacheNames = "view", cacheManager = "redisCacheManager")
    // redis 조회수 중복 검사
    public boolean checkRedis(long contestId, String clientId){
        String key = CLIENT_VIEW_KEY+contestId+":"+clientId;
        String isViewed = redisClient.getValue(key);

        if(isViewed.isEmpty()){
            redisClient.setValue(key,"true", 60 * 24L);
            incrementView(contestId);
            return true;
        }
        return false;
    }
    @Cacheable(cacheNames = "view", cacheManager = "redisCacheManager")
    // 비로그인 cookie 검사
    public boolean checkCookie(long contestId, HttpServletRequest request, HttpServletResponse response){
        String uuid = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    uuid = cookie.getValue();
                    break;
                }
            }
        }

        // 쿠키에 UUID가 없는 경우 새로운 UUID 생성
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            Cookie newCookie = new Cookie(COOKIE_NAME, uuid);
            newCookie.setMaxAge(60 * 60 * 24); // 유효기간 : 1일
            newCookie.setPath("/");
            response.addCookie(newCookie);
        }

        // redis 조회수 중복 검사
        return checkRedis(contestId, uuid);
    }

    // redis 조회수 증가
    private void incrementView(Long id) {
        String redisKey = CONTEST_VIEW_KEY + id;
        redisClient.incrementValue(redisKey);
        String idStr = redisKey.substring(CONTEST_VIEW_KEY.length());
        System.out.println(idStr.split(":")[0]);
    }
    @Cacheable(cacheNames = "view", cacheManager = "redisCacheManager")
    // 조회수 저장
    public void saveView() {
        // Redis에서 VIEW_KEY로 시작하는 모든 키 검색
        List<String> keys = redisClient.scanKeys(CONTEST_VIEW_KEY + "*");

        for (String redisKey : keys) {
            String viewStr = redisClient.getValue(redisKey);

            if (!viewStr.isEmpty()) {
                int view= Integer.parseInt(viewStr);

                Long contestId = extractContestId(redisKey);

                Contest contest = contestRepository.findById(contestId).orElseThrow(()-> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
                contestRepository.updateView(contest.getView() + view);
                contestRepository.save(contest);

                // Redis에서 값 초기화
                redisClient.deleteValue(redisKey);
            }
        }
    }

    private Long extractContestId(String redisKey) {
        String idStr = redisKey.substring(CONTEST_VIEW_KEY.length());
        System.out.println(idStr.split(":")[0]);
        return Long.parseLong(idStr.split(":")[0]);
    }
}
