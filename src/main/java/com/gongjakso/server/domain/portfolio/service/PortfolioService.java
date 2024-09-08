package com.gongjakso.server.domain.portfolio.service;

import static com.gongjakso.server.domain.member.entity.QMember.member;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.portfolio.dto.request.PortfolioReq;
import com.gongjakso.server.domain.portfolio.dto.response.PortfolioRes;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData.Award;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData.Activity;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData.Certificate;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData.Education;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData.Sns;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData.Work;
import com.gongjakso.server.domain.portfolio.repository.PortfolioRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import com.gongjakso.server.global.security.PrincipalDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    // PortfolioReq -> PortfolioData 변환
    private PortfolioData convertToPortfolioData(PortfolioReq portfolioReq) {
        List<PortfolioData.Education> educationList = portfolioReq.educationList() != null
                ? portfolioReq.educationList().stream()
                .map(education -> new PortfolioData.Education(
                        education.school(),
                        education.grade(),
                        education.isActive()
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
        PortfolioData portfolioData = convertToPortfolioData(portfolioReq);
        Portfolio portfolio = Portfolio.builder()
                .member(member)
                .portfolioData(portfolioData)
                .build();
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        return PortfolioRes.from(savedPortfolio);
    }

    @Transactional
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
        PortfolioData updatedPortfolioData = convertToPortfolioData(portfolioReq);
        portfolio.update(updatedPortfolioData);
        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);

        return PortfolioRes.from(updatedPortfolio);
    }

    @Transactional
    public void deletePortfolio(Member member, Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PORTFOLIO_NOT_FOUND_EXCEPTION));
        if (!portfolio.getMember().getId().equals(member.getId())) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_EXCEPTION);
        }
        portfolioRepository.delete(portfolio);
    }
}