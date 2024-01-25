package com.zenyatta.challenge.avature.service;

import static com.zenyatta.challenge.avature.util.Utils.listOfStringsToSet;

import com.zenyatta.challenge.avature.dto.extrasource.ExtraSourceResponseDTO;
import com.zenyatta.challenge.avature.exception.JobberException;
import com.zenyatta.challenge.avature.model.JobPosting;
import com.zenyatta.challenge.avature.model.Location;
import com.zenyatta.challenge.avature.model.Skill;
import com.zenyatta.challenge.avature.rest.JobberwockyExtraRestClient;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobberwockyExtraSourceService {
    private final JobberwockyExtraRestClient jobberwockyExtraRestClient;

    public List<JobPosting> getExternalJobs() throws JobberException {
        List<ExtraSourceResponseDTO> response;

        try {
            response = jobberwockyExtraRestClient.getExternalJobs("");
        } catch (final Exception exception) {
            log.error("Failure fetching external jobs", exception);
            response = new ArrayList<>();
        }

        final List<JobPosting> returnValue = new ArrayList<>();

        for (final ExtraSourceResponseDTO item : response) {
            returnValue.add(
                    new JobPosting(
                            null,
                            item.getJobTitle(),
                            item.getSalary(),
                            new Location(item.getLocation()),
                            listOfStringsToSet(item.getSkills(), Skill.class)));
        }

        return returnValue;
    }
}
