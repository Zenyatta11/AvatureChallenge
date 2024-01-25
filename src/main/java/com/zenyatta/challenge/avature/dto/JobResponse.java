package com.zenyatta.challenge.avature.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zenyatta.challenge.avature.json.JobResponseSerializer;
import com.zenyatta.challenge.avature.model.JobPosting;
import com.zenyatta.challenge.avature.model.Location;
import com.zenyatta.challenge.avature.model.Skill;
import java.util.Set;

@JsonSerialize(using = JobResponseSerializer.class)
public record JobResponse(
        Long id,
        String title,
        Location location,
        Integer salary,
        Set<Skill> skills) {
    public static JobResponse buildFromJobPosting(final JobPosting job) {
        return new JobResponse(job.getId(), job.getTitle(), job.getLocation(),
                job.getSalary(), job.getSkills());
    }
}
