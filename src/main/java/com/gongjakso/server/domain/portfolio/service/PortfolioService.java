package com.gongjakso.server.domain.portfolio.service;

import com.gongjakso.server.domain.portfolio.dto.request.PortfolioReq;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData.Award;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData.Activity;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData.Certificate;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData.Education;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData.Sns;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData.Work;
import com.gongjakso.server.domain.portfolio.repository.PortfolioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    @Transactional
    public Portfolio registerPortfolio(PortfolioReq portfolioReq) {
        List<Education> educationList = portfolioReq.educationList() != null
                ? portfolioReq.educationList().stream()
                .map(education -> new Education(
                        education.school(),
                        education.grade(),
                        education.isActive()
                ))
                .toList()
                : List.of(); // null일 경우 빈 리스트 반환;
        List<PortfolioData.Work> workList = portfolioReq.workList() != null
                ? portfolioReq.workList().stream()
                .map(work -> new Work(
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
                .map(activty -> new Activity(
                        activty.name(),
                        activty.isActive()
                ))
                .toList()
                : List.of();
        List<PortfolioData.Award> awardList = portfolioReq.awardList() != null
                ? portfolioReq.awardList().stream()
                .map(award -> new Award(
                        award.contestName(),
                        award.awardName(),
                        award.awardDate()
                ))
                .toList()
                : List.of();
        List<PortfolioData.Certificate> certificateList = portfolioReq.certificateList() != null
                ? portfolioReq.certificateList().stream()
                .map(certificate -> new Certificate(
                        certificate.name(),
                        certificate.rating(),
                        certificate.certificationDate()
                ))
                .toList()
                : List.of();
        List<PortfolioData.Sns> snsList = portfolioReq.snsList() != null
                ? portfolioReq.snsList().stream()
                .map(sns -> new Sns(
                        sns.snsLink()
                ))
                .toList()
                : List.of();
        PortfolioData portfolioData = new PortfolioData(educationList, workList, activityList, awardList, certificateList, snsList);
        Portfolio portfolio = Portfolio.builder()
                .portfolioData(portfolioData)
                .build();

        return portfolioRepository.save(portfolio);
    }
}
