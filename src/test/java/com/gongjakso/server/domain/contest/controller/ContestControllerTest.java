package com.gongjakso.server.domain.contest.controller;

import com.gongjakso.server.domain.member.service.MemberService;
import com.gongjakso.server.global.security.PrincipalDetails;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(ContestControllerTest.class)
public class ContestControllerTest {
    @Autowired
    MockMvc mockMvc;
    WebApplicationContext context;
    @MockBean
    MemberService memberService;
    @MockBean
    PrincipalDetails principalDetails;
    @BeforeEach
    void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }
}
