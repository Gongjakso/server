package com.gongjakso.server.domain.contest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongjakso.server.domain.contest.dto.request.ContestReq;
import com.gongjakso.server.domain.contest.dto.request.UpdateContestDto;
import com.gongjakso.server.domain.contest.dto.response.ContestListRes;
import com.gongjakso.server.domain.contest.dto.response.ContestRes;
import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.contest.mock.WithCustomMockUser;
import com.gongjakso.server.domain.contest.service.ContestService;
import com.gongjakso.server.domain.contest.util.ContestUtilTest;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.enumerate.MemberType;
import com.gongjakso.server.domain.member.service.MemberService;
import com.gongjakso.server.domain.member.util.MemberUtilTest;
import com.gongjakso.server.global.config.SecurityConfig;
import com.gongjakso.server.global.security.PrincipalDetails;
import com.gongjakso.server.global.security.jwt.TokenProvider;
import com.gongjakso.server.global.util.redis.RedisClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ContestController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebAppConfiguration
public class ContestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ContestService contestService;
    @MockBean
    PrincipalDetails principalDetails;
    @MockBean
    TokenProvider tokenProvider;
    @MockBean
    RedisClient redisClient;
    @MockBean
    HttpServletRequest httpServletRequest;
    @MockBean
    HttpServletResponse httpServletResponse;

    @Test
    @WithCustomMockUser
    @DisplayName("공모전 생성 api (관리자만 요청)")
    void createContest() throws Exception{
        Member adminMember = MemberUtilTest.buildMemberByType(MemberType.ADMIN);
        ContestReq contestReq = ContestUtilTest.buildContestReq();
        given(principalDetails.getMember()).willReturn(adminMember);

        final String fileName = "1"; //파일명
        final String contentType = "avif"; //파일타입

        FileInputStream fileInputStream = new FileInputStream("src/test/java/com/gongjakso/server/domain/contest/image/"+fileName+"."+contentType);
        MockMultipartFile image = new MockMultipartFile(
                "image", //name
                fileName + "." + contentType, //originalFilename
                contentType,
                fileInputStream
        );

        mockMvc.perform(multipart("/api/v2/contest")
                        .file(image)
                        .header("Authorization","Bearer Token token")
                        .param("contestReq", new ObjectMapper().writeValueAsString(contestReq)) //param은 문자열로만 데이터 전송이 가능하므로 json문자열로 변환
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(contestService).save(adminMember,any(MultipartFile.class),eq(contestReq));

    }

    @Test
    @DisplayName("공모전 정보 API")
    void findContestTest() throws Exception {
        // given
        ContestRes contestRes = ContestUtilTest.buildContestResDto();
        given(contestService.find(anyLong(), any(HttpServletRequest.class), any(HttpServletResponse.class)))
                .willReturn(contestRes);

        // when
        mockMvc.perform(get("/api/v2/contest/{contest_id}", anyLong()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("공작소 title"));

        // then
        verify(contestService).find(anyLong(), any(HttpServletRequest.class), any(HttpServletResponse.class));
    }

    @Test
    @DisplayName("공모전 검색 API")
    void searchContestTest() throws Exception {
        // given
        ContestListRes listRes = new ContestListRes(anyList(),anyInt(),anyLong(),anyInt());

        given(contestService.search(anyString(), anyString(), any(Pageable.class)))
                .willReturn(listRes);

        // when
        mockMvc.perform(get("/api/v2/contest/search")
                        .param("word", "공모전")
                        .param("sortAt", "createdAt")
                        .param("page", "0")
                        .param("size", "12"))
                .andExpect(status().isOk());

        // then
        verify(contestService).search(anyString(), anyString(), any(Pageable.class));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("공모전 수정 API - 관리자만")
    void updateContestTest() throws Exception {
        // given
        final String fileName = "1"; //파일명
        final String contentType = "avif"; //파일타입
        Member adminMember = MemberUtilTest.buildMemberByType(MemberType.ADMIN);
        UpdateContestDto updateDto = ContestUtilTest.buildUpdateContestDto();
        Long contestId = 1L;
        ContestRes updatedContestRes = ContestUtilTest.buildContestResDto();
        given(principalDetails.getMember()).willReturn(adminMember);
        given(contestService.update(adminMember, contestId, any(MultipartFile.class), eq(updateDto)))
                .willReturn(updatedContestRes);

        FileInputStream fileInputStream = new FileInputStream("src/test/java/com/gongjakso/server/domain/contest/image/"+fileName+"."+contentType);
        MockMultipartFile image = new MockMultipartFile(
                "image", //name
                fileName + "." + contentType, //originalFilename
                contentType,
                fileInputStream
        );

        // when
        mockMvc.perform(multipart("/api/v2/contest/{contest_id}", contestId)
                        .file(image)
                        .param("contestReq", new ObjectMapper().writeValueAsString(updateDto))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Updated Test Contest"));

        // then
        verify(contestService).update(adminMember, contestId, any(MultipartFile.class), eq(updateDto));
    }
}
