package com.gongjakso.server.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongjakso.server.domain.apply.service.ApplyService;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.repository.MemberRepository;
import com.gongjakso.server.domain.member.util.MemberUtilTest;
import com.gongjakso.server.domain.post.dto.PostScrapRes;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.domain.post.service.PostService;
import com.gongjakso.server.domain.post.util.PostScrapUtilTest;
import com.gongjakso.server.domain.post.util.PostUtilTest;
import com.gongjakso.server.global.security.PrincipalDetails;
import com.gongjakso.server.global.security.jwt.JwtFilter;
import com.gongjakso.server.global.security.jwt.TokenProvider;
import com.gongjakso.server.global.util.redis.RedisClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
@AutoConfigureMockMvc
@Import(JpaMetamodelMappingContext.class)
@ExtendWith(MockitoExtension.class)
public class PaginationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper; //response를 json으로 바꿔주기위해 필요한 의존성
    @MockBean
    private PostService postService;
    @MockBean
    private PostRepository postRepository;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private ApplyService applyService;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private RedisClient redisClient;
    @MockBean
    private JwtFilter jwtFilter;

    private Member member;
    private PrincipalDetails principalDetails;

    @BeforeEach
    void setUp() {
        member = MemberUtilTest.buildMemberAndId(1L);
        principalDetails = new PrincipalDetails(member);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principalDetails, null, principalDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("공고 스크랩 저장 및 취소 기능")
    void scrapPost() throws Exception {
        //given
        Post post = PostUtilTest.builderPost(1L, member);
        PostScrapRes postScrapRes = PostScrapUtilTest.ScrapPostRes(post.getPostId(), member.getMemberId(), true);
        given(postService.scrapPost(any(Member.class), anyLong())).willReturn(postScrapRes);
        String resJson = objectMapper.writeValueAsString(postScrapRes);

        mockMvc.perform(post("/api/v1/post/{id}", post.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal((Principal) principalDetails))
                .andExpect(status().isOk())
                .andExpect(content().json(resJson))
                .andDo(print());

        verify(postService).scrapPost(any(Member.class), any(Long.class));

    }

}
