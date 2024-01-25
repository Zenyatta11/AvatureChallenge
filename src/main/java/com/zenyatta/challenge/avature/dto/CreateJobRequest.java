package com.zenyatta.challenge.avature.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.zenyatta.challenge.avature.json.CreateJobRequestDeserializer;
import com.zenyatta.challenge.avature.model.JobPosting;
import com.zenyatta.challenge.avature.model.Location;
import com.zenyatta.challenge.avature.model.Skill;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@JsonDeserialize(using = CreateJobRequestDeserializer.class)
public class CreateJobRequest {
    private @NotNull String title;
    private @NotNull Integer salary;
    private @NotNull Location location;
    private Set<Skill> skills;

    public static JobPosting buildJobRequest(final CreateJobRequest request) {
        return JobPosting.builder()
                .title(request.title)
                .salary(request.salary)
                .location(request.location)
                .skills(request.skills)
                .build();
    }
}
