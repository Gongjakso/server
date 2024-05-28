package com.gongjakso.server.domain.post.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class PaginationControllerTest {
/*
    MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

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

 */
}
