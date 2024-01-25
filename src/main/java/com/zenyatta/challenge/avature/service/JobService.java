package com.zenyatta.challenge.avature.service;

import static com.zenyatta.challenge.avature.util.Utils.processEntitySet;

import com.zenyatta.challenge.avature.dto.JobResponse;
import com.zenyatta.challenge.avature.dto.filter.JobFilter;
import com.zenyatta.challenge.avature.dto.util.ListPaginatedResponse;
import com.zenyatta.challenge.avature.exception.JobberException;
import com.zenyatta.challenge.avature.model.JobPosting;
import com.zenyatta.challenge.avature.repository.JobPostingRepository;
import com.zenyatta.challenge.avature.repository.LocationRepository;
import com.zenyatta.challenge.avature.repository.SkillRepository;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobPostingRepository repository;
    private final LocationRepository locationRepository;
    private final SkillRepository skillRepository;
    private final JobberwockyExtraSourceService extraSourceService;

    public JobPosting createJobPosting(final JobPosting jobPosting) throws JobberException {
        jobPosting.setLocation(processEntitySet(jobPosting.getLocation(), locationRepository));
        jobPosting.setSkills(processEntitySet(jobPosting.getSkills(), skillRepository));

        return save(jobPosting.toBuilder().build());
    }

    @Transactional(rollbackFor = Exception.class)
    public JobPosting save(@NotNull final JobPosting jobPosting) {
        return repository.save(jobPosting);
    }

    /**
     * This seemlingly mess-of-a-method will first fetch job postings in the local
     * database and then, if there is still room in the page, fetch from the
     * external service. We don't want to overflow the page so only add the amount
     * of jobs that are still within the specified page parameters.
     **/
    public ListPaginatedResponse<JobResponse> searchJobPostings(final Pageable pageable, final JobFilter filter)
            throws JobberException {
        final Page<JobResponse> page = repository
                .findAll(filter.toSpecifications(), pageable)
                .map(JobResponse::buildFromJobPosting);

        final int remainingPageSize = pageable.getPageSize() - page.getNumberOfElements();

        if (remainingPageSize > 0) {
            final List<JobPosting> externalJobs = extraSourceService.getExternalJobs();

            final int numberOfJobsToAdd = Math.min(remainingPageSize, externalJobs.size());
            final List<JobResponse> externalJobResponses = externalJobs.stream()
                    .filter(filter.toPredicate())
                    .limit(numberOfJobsToAdd)
                    .map(JobResponse::buildFromJobPosting)
                    .collect(Collectors.toList());

            final List<JobResponse> combinedResponses = new ArrayList<>(page.getContent());
            combinedResponses.addAll(externalJobResponses);

            final Page<JobResponse> combinedPage = new PageImpl<>(combinedResponses, pageable,
                    combinedResponses.size());

            return new ListPaginatedResponse<>(combinedPage);
        }

        return new ListPaginatedResponse<>(page);
    }
}
