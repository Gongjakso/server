package com.gongjakso.server.domain.portfolio.service;

import com.gongjakso.server.domain.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

}
