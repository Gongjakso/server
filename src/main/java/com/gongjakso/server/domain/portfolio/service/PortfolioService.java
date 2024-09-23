package com.gongjakso.server.domain.portfolio.service;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.portfolio.dto.request.PortfolioReq;
import com.gongjakso.server.domain.portfolio.dto.response.ExistPortfolioRes;
import com.gongjakso.server.domain.portfolio.dto.response.PortfolioRes;
import com.gongjakso.server.domain.portfolio.dto.response.SimplePortfolioRes;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import com.gongjakso.server.domain.portfolio.enumerate.DataType;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData;
import com.gongjakso.server.domain.portfolio.repository.PortfolioRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import java.util.List;

import com.gongjakso.server.global.util.s3.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final S3Client s3Client;
    private final String S3_PORTFOLIO_DIR_NAME = "portfolio";


    // PortfolioName 생성 로직을 분리
    private String generatePortfolioName(String portfolioName) {
        if (portfolioName == null || portfolioName.isEmpty()) {
            long existingPortfolioCount = portfolioRepository.countByDeletedAtIsNull();
            return "포트폴리오 " + (existingPortfolioCount + 1);
        }
        return portfolioName;
    }

    // PortfolioReq -> PortfolioData 변환
    private PortfolioData convertToPortfolioData(PortfolioReq portfolioReq) {
        List<PortfolioData.Education> educationList = portfolioReq.educationList() != null
                ? portfolioReq.educationList().stream()
                .map(education -> new PortfolioData.Education(
                        education.school(),
                        education.grade(),
                        education.state()
                ))
                .toList()
                : List.of();

        List<PortfolioData.Work> workList = portfolioReq.workList() != null
                ? portfolioReq.workList().stream()
                .map(work -> new PortfolioData.Work(
                        work.company(),
                        work.partition(),
                        work.enteredAt(),
                        work.exitedAt(),
                        work.isActive(),
                        work.detail()
                ))
                .toList()
                : List.of();

        List<PortfolioData.Activity> activityList = portfolioReq.activityList() != null
                ? portfolioReq.activityList().stream()
                .map(activity -> new PortfolioData.Activity(
                        activity.name(),
                        activity.isActive()
                ))
                .toList()
                : List.of();

        List<PortfolioData.Award> awardList = portfolioReq.awardList() != null
                ? portfolioReq.awardList().stream()
                .map(award -> new PortfolioData.Award(
                        award.contestName(),
                        award.awardName(),
                        award.awardDate()
                ))
                .toList()
                : List.of();

        List<PortfolioData.Certificate> certificateList = portfolioReq.certificateList() != null
                ? portfolioReq.certificateList().stream()
                .map(certificate -> new PortfolioData.Certificate(
                        certificate.name(),
                        certificate.rating(),
                        certificate.certificationDate()
                ))
                .toList()
                : List.of();

        List<PortfolioData.Sns> snsList = portfolioReq.snsList() != null
                ? portfolioReq.snsList().stream()
                .map(sns -> new PortfolioData.Sns(
                        sns.snsLink()
                ))
                .toList()
                : List.of();

        return new PortfolioData(educationList, workList, activityList, awardList, certificateList, snsList);
    }

    @Transactional
    public PortfolioRes registerPortfolio(Member member, PortfolioReq portfolioReq) {
        if (member == null) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        String portfolioName = generatePortfolioName(portfolioReq.portfolioName());
        PortfolioData portfolioData = convertToPortfolioData(portfolioReq);

        Portfolio portfolio = new Portfolio(member,portfolioName,portfolioData);

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        return PortfolioRes.from(savedPortfolio);
    }

    public PortfolioRes getPortfolio(Member member, Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findByIdAndDeletedAtIsNull(portfolioId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PORTFOLIO_NOT_FOUND_EXCEPTION));
        if (!portfolio.getMember().getId().equals(member.getId())) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_EXCEPTION);
        }

        return PortfolioRes.from(portfolio);
    }

    @Transactional
    public PortfolioRes updatePortfolio(Member member, Long portfolioId, PortfolioReq portfolioReq) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PORTFOLIO_NOT_FOUND_EXCEPTION));
        if (!portfolio.getMember().getId().equals(member.getId())) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_EXCEPTION);
        }

        if (portfolioReq.portfolioName() != null) {
            String portfolioName = generatePortfolioName(portfolioReq.portfolioName());
            portfolio.updateName(portfolioName);
        }

        PortfolioData updatedPortfolioData = convertToPortfolioData(portfolioReq);
        portfolio.updateData(updatedPortfolioData);

        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);

        return PortfolioRes.from(updatedPortfolio);
    }

    @Transactional
    public void deletePortfolio(Member member, Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PORTFOLIO_NOT_FOUND_EXCEPTION));
        if (portfolio.getDeletedAt() != null) {
            throw new ApplicationException(ErrorCode.ALREADY_DELETE_EXCEPTION);
        }
        if (!portfolio.getMember().getId().equals(member.getId())) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_EXCEPTION);
        }
        portfolioRepository.delete(portfolio);
    }

    public List<SimplePortfolioRes> getMyPortfolios(Member member) {
        List<Portfolio> portfolioList = portfolioRepository.findByMemberAndDeletedAtIsNull(member);

        if (portfolioList.isEmpty()) {
            return List.of(SimplePortfolioRes.of(null, false, null));
        }

        return portfolioList.stream()
                .map(portfolio -> SimplePortfolioRes.of(portfolio, true, portfolio.getFileUri() != null || portfolio.getNotionUri() != null))
                .toList();
    }

    @Transactional
    public void saveExistPortfolio(Member member, MultipartFile file, String notionUri){
        //등록된 파일이나 노션 링크 있는지 확인
        //Validation
        Boolean isExist = portfolioRepository.existsExistPortfolioByMember(member);
        if (isExist){
            throw new ApplicationException(ErrorCode.ALREADY_EXIST_EXCEPTION);
        }
        if (file == null && notionUri == null){
            throw new ApplicationException(ErrorCode.PORTFOLIO_SAVE_FAILED_EXCEPTION);
        }
        //Business
        String s3Url = null;
        if ( file!=null ) {
            s3Url = s3Client.upload(file, S3_PORTFOLIO_DIR_NAME);
        }
        Portfolio portfolio = new Portfolio(member,generatePortfolioName(null),s3Url,notionUri);
        portfolioRepository.save(portfolio);
    }

    @Transactional
    public void deleteExistPortfolio(Member member, Long id){
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(()-> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(!member.getId().equals(portfolio.getMember().getId())){
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        if(portfolio.getFileUri() != null || !portfolio.getFileUri().isEmpty()){
            s3Client.delete(portfolio.getFileUri());
        }
        portfolioRepository.delete(portfolio);
    }

    @Transactional
    public void updateExistPortfolio(Member member, Long id, MultipartFile file, String notionUri){
        //등록된 파일이나 노션 링크 있는지 확인
        //Validation
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(()-> new ApplicationException(ErrorCode.PORTFOLIO_NOT_FOUND_EXCEPTION));
        if(!member.getId().equals(portfolio.getMember().getId())){
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        //Business
        String s3Url = null;
        if ( file!=null ) {
            if(portfolio.getFileUri()!=null && !portfolio.getFileUri().isEmpty()){
                s3Client.delete(portfolio.getFileUri());
            }
            s3Url = s3Client.upload(file, S3_PORTFOLIO_DIR_NAME);
        }
        portfolio.updatePortfolioUri(portfolio,s3Url,notionUri);
        portfolioRepository.save(portfolio);
    }

    public ExistPortfolioRes findExistPorfolio(Member member, Long id, DataType dataType){
        //Validation
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(()-> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        if (!portfolio.getMember().getId().equals(member.getId())) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_EXCEPTION);
        }
        if (dataType.equals(DataType.FILE)){
            return new ExistPortfolioRes(null,portfolio.getNotionUri());
        } else if (dataType.equals(DataType.NOTION)) {
            return new ExistPortfolioRes(portfolio.getFileUri(),null);
        }else {
            return new ExistPortfolioRes(portfolio.getFileUri(),portfolio.getNotionUri());
        }
    }

}