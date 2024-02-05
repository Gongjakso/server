package com.gongjakso.server;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.repository.ApplyRepository;
import com.gongjakso.server.domain.member.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplyControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ApplyRepository applyRepository;
    @BeforeEach
    public void setMockMvc(){
        this.mockMvc= MockMvcBuilders.webAppContextSetup(context).build();
    }
    @AfterEach
    public void cleanUp(){
        applyRepository.deleteAll();
    }

    @DisplayName("createApply : 지원하기에 성공한다.")
    @Test
    public void createApply() throws Exception{
        final String url = "/api/v1/apply/1";
//        Apply savedApply = applyRepository.save(new Apply(1L, ))

    }
}
