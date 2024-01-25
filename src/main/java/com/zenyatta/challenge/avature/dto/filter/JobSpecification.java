package com.zenyatta.challenge.avature.dto.filter;

import com.zenyatta.challenge.avature.model.JobPosting;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Locale;
import org.springframework.data.jpa.domain.Specification;

public final class JobSpecification {

    private JobSpecification() {
    }

    public static Specification<JobPosting> searchByName(final String name) {
        return (job, query, builder) -> builder.like(
                builder.lower(job.get("title")),
                "%" + name.toLowerCase(Locale.getDefault()) + "%");
    }

    public static Specification<JobPosting> minSalary(final Integer number) {
        return (job, query, builder) -> builder.greaterThanOrEqualTo(
                job.get("salary"),
                number);
    }

    public static Specification<JobPosting> maxSalary(final Integer number) {
        return (job, query, builder) -> builder.lessThan(
                job.get("salary"),
                number);
    }

    public static Specification<JobPosting> searchByLocation(final String location) {
        return (job, query, builder) -> builder.like(
                builder.lower(job.get("location")),
                "%" + location.toLowerCase(Locale.getDefault()) + "%");
    }

    public static Specification<JobPosting> searchBySkills(final List<String> skillNames) {
        return (job, query, builder) -> {
            query.distinct(true);
            job.fetch("skills", JoinType.LEFT);

            final Predicate[] predicates = skillNames.stream()
                    .map(skillName -> builder.like(
                            builder.lower(job.join("skills").get("name")),
                            "%" + skillName.toLowerCase(Locale.getDefault()) + "%"))
                    .toArray(Predicate[]::new);

            return builder.and(predicates);
        };
    }
}
