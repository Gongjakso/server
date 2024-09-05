package com.gongjakso.server.domain.portfolio.entity;

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

    @Column(name = "portfolio_data", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private PortfolioData portfolioData;

    @Builder
    public Portfolio(PortfolioData portfolioData) {
        this.portfolioData = portfolioData;
    }
}
