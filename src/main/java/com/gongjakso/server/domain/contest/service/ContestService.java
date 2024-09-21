package com.gongjakso.server.domain.contest.service;

import com.gongjakso.server.domain.contest.dto.request.ContestReq;
import com.gongjakso.server.domain.contest.dto.request.UpdateContestDto;
import com.gongjakso.server.domain.contest.dto.response.ContestCard;
import com.gongjakso.server.domain.contest.dto.response.ContestListRes;
import com.gongjakso.server.domain.contest.dto.response.ContestRes;
import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.contest.repository.ContestRepository;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.enumerate.MemberType;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import com.gongjakso.server.global.util.s3.S3Client;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;
    private final S3Client s3Client;

    private final String S3_CONTEST_DIR_NAME = "contest";


    @Transactional
    public void save(Member member, MultipartFile image,ContestReq contestReq){
        // Validation
        if(!member.getMemberType().equals(MemberType.ADMIN)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        //Business
        //image s3에 올리기
        String s3Url = null;
        if (image != null && !image.isEmpty()) {
            s3Url = s3Client.upload(image, S3_CONTEST_DIR_NAME);
        }
        //contest body data \n-><br/> \t-><t/>
        String body = contestReq.body().replace("\n","<br/>")
                                        .replace("\t","<t/>");
        //contest build 및 생성
        Contest contest = Contest.builder()
                .title(contestReq.title())
                .body(body)
                .contestLink(contestReq.contestLink())
                .institution(contestReq.institution())
                .startedAt(contestReq.startedAt())
                .finishedAt(contestReq.finishedAt())
                .imgUrl(s3Url)
                .view(0)
                .build();
        contestRepository.save(contest);
    }

    @Transactional
    public ContestRes find(Long id, HttpServletRequest request, HttpServletResponse response){
        //Vaildation
        Contest contest = contestRepository.findById(id).orElseThrow(()-> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        //Business
        //조회수 업데이트
        updateView(contest,request,response);
        //Response
        return ContestRes.of(contest);
    }

    @Transactional
    public ContestRes update(Member member, Long id,MultipartFile image,UpdateContestDto updateContestDto){
        // Validation
        if(!member.getMemberType().equals(MemberType.ADMIN)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        Contest contest = contestRepository.findById(id).orElseThrow(()-> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        //Business
        String imgUrl = null;
        if(image!=null && !image.isEmpty()){
            s3Client.delete(contest.getImgUrl());
            imgUrl= s3Client.upload(image,S3_CONTEST_DIR_NAME);
        }
        contest.update(updateContestDto,imgUrl);
        contestRepository.save(contest);
        //Response
        return ContestRes.of(contest);
    }

    @Transactional
    public void delete(Member member,Long id){
        // Validation
        if(!member.getMemberType().equals(MemberType.ADMIN)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        Contest contest = contestRepository.findById(id).orElseThrow(()-> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        //Business
        s3Client.delete(contest.getImgUrl());
        contestRepository.delete(contest);
    }

    @Transactional
    public ContestListRes search(String word, String sortAt, Pageable pageable){
        //Business
        Page<Contest> contestPage = contestRepository.searchList(word, sortAt, pageable);
        List<ContestCard> list = new ArrayList<>();
        contestPage.getContent().forEach(contest-> list.add(ContestCard.of(contest)));
        //Response
        return ContestListRes.of(list,contestPage.getNumber(),contestPage.getTotalElements(), contestPage.getTotalPages());
    }

    public void updateView(Contest contest, HttpServletRequest request, HttpServletResponse response) {
        boolean hasViewed = false;
        Cookie[] cookies = request.getCookies();

        String COOKIE_NAME = "contest_view";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    hasViewed = true;
                    break;
                }
            }
        }

        if (!hasViewed) {
            contest.updateView(contest);
            // 세션 쿠키 설정
            Cookie newCookie = new Cookie(COOKIE_NAME, "viewed");
            newCookie.setMaxAge(-1); // 브라우저 세션이 끝날 때까지 유효
            newCookie.setPath("/");
            response.addCookie(newCookie);
        }
    }
}
