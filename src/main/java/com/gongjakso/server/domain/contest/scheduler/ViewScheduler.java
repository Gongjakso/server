package com.gongjakso.server.domain.contest.scheduler;

import com.gongjakso.server.domain.contest.service.ViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViewScheduler {

    private final ViewService viewService;

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void updateView() {
        viewService.saveView();
    }
}
