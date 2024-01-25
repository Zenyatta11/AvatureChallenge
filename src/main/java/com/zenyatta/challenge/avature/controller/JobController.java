package com.zenyatta.challenge.avature.controller;

import com.zenyatta.challenge.avature.dto.CommandResponse;
import com.zenyatta.challenge.avature.dto.CreateAlertRequest;
import com.zenyatta.challenge.avature.dto.CreateJobRequest;
import com.zenyatta.challenge.avature.dto.JobResponse;
import com.zenyatta.challenge.avature.dto.ResponseStatus;
import com.zenyatta.challenge.avature.dto.filter.JobFilter;
import com.zenyatta.challenge.avature.dto.util.ListPaginatedResponse;
import com.zenyatta.challenge.avature.exception.JobberException;
import com.zenyatta.challenge.avature.model.Alert;
import com.zenyatta.challenge.avature.model.JobPosting;
import com.zenyatta.challenge.avature.service.AlertService;
import com.zenyatta.challenge.avature.service.JobService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService service;
    private final AlertService alertService;

    @PostMapping("/subscribe")
    public CommandResponse sendAlert(@RequestBody @Valid final CreateAlertRequest request) throws JobberException {
        final Alert alert = CreateAlertRequest.buildAlertRequest(request);
        alertService.createAlert(alert);

        return CommandResponse.buildJobResponse(null, ResponseStatus.SUCCEEDED, "");
    }

    @PostMapping
    public CommandResponse createJobPosting(@RequestBody @Valid final CreateJobRequest request) throws JobberException {
        final JobPosting jobPosting = CreateJobRequest.buildJobRequest(request);
        final JobPosting resultJobPosting = service.createJobPosting(jobPosting);

        return CommandResponse.buildJobResponse(resultJobPosting.getId(), ResponseStatus.SUCCEEDED, "");
    }

    @GetMapping
    public ListPaginatedResponse<JobResponse> searchJobPostings(
            @RequestParam(required = false) final String name,
            @RequestParam(required = false) final Integer salaryMin,
            @RequestParam(required = false) final Integer salaryMax,
            @RequestParam(required = false) final String country,
            @RequestParam(required = false) final List<String> skills,
            @RequestParam(defaultValue = "0") @Min(0) final Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) final Integer pageSize) throws JobberException {
        final Pageable pageable = PageRequest.of(
                page,
                pageSize);

        final JobFilter filter = new JobFilter(
                name,
                salaryMin,
                salaryMax,
                country,
                skills);

        return service.searchJobPostings(pageable, filter);
    }

}
