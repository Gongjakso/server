package com.gongjakso.server.domain.portfolio.controller;

import com.gongjakso.server.domain.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/mypage/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;
}
