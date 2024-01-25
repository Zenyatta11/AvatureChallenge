package com.zenyatta.challenge.avature.dto.filter;

import com.zenyatta.challenge.avature.model.JobPosting;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import org.springframework.data.jpa.domain.Specification;

public record JobFilter(
        String name,
        Integer salaryMin,
        Integer salaryMax,
        String location,
        List<String> skills) {

    public Specification<JobPosting> toSpecifications() {
        Specification<JobPosting> specs = Specification.where(null);

        if (name != null) {
            specs = specs.and(JobSpecification.searchByName(name));
        }

        if (salaryMin != null) {
            specs = specs.and(JobSpecification.minSalary(salaryMin));
        }

        if (salaryMax != null) {
            specs = specs.and(JobSpecification.maxSalary(salaryMin));
        }

        if (location != null) {
            specs = specs.and(JobSpecification.searchByLocation(location));
        }

        if (skills != null && !skills.isEmpty()) {
            specs = specs.and(JobSpecification.searchBySkills(skills));
        }

        return specs;
    }

    public Predicate<JobPosting> toPredicate() {
        return job -> {
            boolean nameMatch = true;
            boolean minSalaryMatch = true;
            boolean maxSalaryMatch = true;
            boolean locationMatch = true;
            boolean skillsMatch = true;

            if (name != null) {
                nameMatch = Optional.of(name).map(
                        n -> job.getTitle().toLowerCase(Locale.getDefault())
                                .contains(n.toLowerCase(Locale.getDefault())))
                        .orElse(true);
            }

            if (salaryMin != null) {
                minSalaryMatch = Optional.of(salaryMin).map(s -> job.getSalary() >= s).orElse(true);
            }

            if (salaryMax != null) {
                maxSalaryMatch = Optional.of(salaryMax).map(s -> job.getSalary() < s).orElse(true);
            }

            if (location != null) {
                locationMatch = Optional.of(location)
                        .map(l -> job.getLocation().getName().toLowerCase(Locale.getDefault())
                                .contains(l.toLowerCase(Locale.getDefault())))
                        .orElse(true);
            }

            if (skills != null && !skills.isEmpty()) {
                skillsMatch = Optional.of(skills).map(skills -> job.getSkills().stream()
                        .anyMatch(skill -> skills.stream()
                                .anyMatch(skillQuery -> skill.getName().toLowerCase(Locale.getDefault())
                                        .contains(skillQuery.toLowerCase(Locale.getDefault())))))
                        .orElse(true);
            }

            return nameMatch && minSalaryMatch && maxSalaryMatch && locationMatch && skillsMatch;
        };
    }
}
