package com.gongjakso.server.global.config;

import com.gongjakso.server.global.util.quartz.UpdateTeamStatusJob;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    private final Scheduler scheduler;

    @PostConstruct
    public void scheduleDailyJob() throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(UpdateTeamStatusJob.class)
                .withIdentity("updateTeamStatusJob", "teamStatusGroup")
                .build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("updateTeamStatusTrigger", "teamStatusGroup")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 0))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
