package com.gongjakso.server.domain.contest.service;

import com.gongjakso.server.domain.contest.dto.request.ContestReq;
import com.gongjakso.server.domain.contest.dto.response.ContestRes;
import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.contest.repository.ContestRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import com.gongjakso.server.global.util.s3.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;
    private final S3Client s3Client;


    @Transactional
    public void save(MultipartFile image,ContestReq contestReq){
        //Business
        //image s3에 올리기
        String s3Url = s3Client.upload(image, "contest");
        //contest build 및 생성
        Contest contest = Contest.builder()
                .title(contestReq.title())
                .body(contestReq.body())
                .contestLink(contestReq.contestLink())
                .institution(contestReq.institution())
                .startedAt(contestReq.startedAt())
                .finishedAt(contestReq.finishedAt())
                .imgUrl(s3Url)
                .build();
        contestRepository.save(contest);
    }

    @Transactional
    public ContestRes find(Long id){
        //Vaildation
        Contest contest = contestRepository.findById(id).orElseThrow(()-> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        //Response
        return ContestRes.of(contest);
    }
}
