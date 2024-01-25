package com.zenyatta.challenge.avature.dto.extrasource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtraSourceResponseDTO implements Serializable {
    private String jobTitle;
    private Integer salary;
    private String location;
    private List<String> skills;
}
