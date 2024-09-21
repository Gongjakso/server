package com.gongjakso.server.domain.portfolio.entity;

import com.gongjakso.server.domain.contest.dto.request.UpdateContestDto;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.SqlTypes;
import org.springframework.data.redis.connection.ReactiveGeoCommands;

@Getter
@Entity
@Table(name = "portfolio")
@SQLDelete(sql = "UPDATE portfolio SET deleted_at = NOW() where portfolio_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Portfolio extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @Column(name = "portfolio_name", nullable = false, columnDefinition = "varchar(50)")
    private String portfolioName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "portfolio_data", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private PortfolioData portfolioData;

    @Column(name = "file_uri",columnDefinition = "text")
    private String fileUri;

    @Column(name = "notion_uri",columnDefinition = "text")
    private String notionUri;

    @Builder(builderMethodName = "portfolioBuilder")
    public Portfolio(Member member, String portfolioName, PortfolioData portfolioData) {
        this.member = member;
        this.portfolioName = portfolioName;
        this.portfolioData = portfolioData;
    }

    @Builder(builderMethodName = "existPortfolioBuilder")
    public Portfolio(Member member, String portfolioName, String fileUri, String notionUri){
        this.member = member;
        this.portfolioName = portfolioName;
        this.fileUri = fileUri;
        this.notionUri = notionUri;
    }
    public void updatePortfolioUri(Portfolio portfolio, String fileUri, String notionUri) {
        this.fileUri = (fileUri == null) ? this.fileUri : fileUri;
        this.notionUri = (notionUri == null) ? this.notionUri : notionUri;
    }


    public void updateName(String updatedName) {
        this.portfolioName = updatedName;
    }

    public void updateData(PortfolioData updatedData) {
        this.portfolioData = updatedData;
    }
}
