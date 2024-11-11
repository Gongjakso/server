package com.gongjakso.server.global.util.quartz;

import com.gongjakso.server.domain.team.service.TeamService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateTeamStatusJob implements Job {

    @Autowired
    private TeamService teamService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        teamService.updateTeamsToActiveStatus();
    }
}