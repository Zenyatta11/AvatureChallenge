package com.zenyatta.challenge.avature.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.zenyatta.challenge.avature.json.CreateAlertRequestDeserializer;
import com.zenyatta.challenge.avature.model.Alert;
import com.zenyatta.challenge.avature.model.Location;
import com.zenyatta.challenge.avature.model.Skill;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@JsonDeserialize(using = CreateAlertRequestDeserializer.class)
public class CreateAlertRequest {
    private @NotNull String email;
    private String title;
    private Integer minSalary;
    private Integer maxSalary;
    private Location location;
    private Set<Skill> skills;

    public static Alert buildAlertRequest(final CreateAlertRequest request) {
        return Alert.builder()
                .email(request.email)
                .title(request.title)
                .minSalary(request.minSalary)
                .maxSalary(request.maxSalary)
                .location(request.location)
                .build();
    }
}
