package com.zenyatta.challenge.avature.repository;

import com.zenyatta.challenge.avature.model.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

public interface JobPostingRepository extends CrudRepository<JobPosting, Long> {
    Page<JobPosting> findAll(Specification<JobPosting> filter, Pageable pageable);
}
