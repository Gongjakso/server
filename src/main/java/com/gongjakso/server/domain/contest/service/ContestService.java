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

    private  final String S3_CONTEST_DIR_NAME = "contest";


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
    public ContestListRes search(String word, String arrange, Pageable pageable){
        //Business
        Page<Contest> contestPage = contestRepository.searchList(word, arrange, pageable);
        List<ContestCard> list = new ArrayList<>();
        contestPage.getContent().forEach(contest-> list.add(ContestCard.of(contest,1)));
        //Response
        return ContestListRes.of(list,contestPage.getNumber(),contestPage.getTotalElements(), contestPage.getTotalPages());
    }
}
