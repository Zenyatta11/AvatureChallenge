package com.zenyatta.challenge.avature.service;

import com.zenyatta.challenge.avature.model.JobPosting;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PrePersist;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EntityInterceptor implements Serializable {

    private final EmailService emailService;

    @Autowired
    public EntityInterceptor(final EmailService emailService) {
        this.emailService = emailService;
    }

    @PrePersist
    public void onObjectCreated(final Object obj) {

        if (obj instanceof JobPosting) {
            final JobPosting jobPosting = (JobPosting) obj;

            final List<String> emailsToNotify = this.getEmailsToAlert(jobPosting);

            for (final String email : emailsToNotify) {
                emailService.sendAlert(jobPosting, email);
            }
        }
    }

    private List<String> getEmailsToAlert(final JobPosting jobPosting) {
        final EntityManager entityManager = BeanUtil.getBean(EntityManager.class);

        try {
            final String queryString = "SELECT DISTINCT a.email FROM job_alerts a " +
                    "WHERE (a.title IS NULL OR LOWER(:title) LIKE '%' || LOWER(a.title) || '%') " +
                    "AND (a.min_salary IS NULL OR a.min_salary >= :salary) " +
                    "AND (a.max_salary IS NULL OR a.max_salary < :salary) " +
                    "AND (a.location_id IS NULL OR a.location_id = :locationId)";

            return entityManager.createNativeQuery(queryString)
                    .setParameter("title", jobPosting.getTitle().toLowerCase(Locale.getDefault()))
                    .setParameter("salary", jobPosting.getSalary())
                    .setParameter("locationId", jobPosting.getLocation().getId())
                    .getResultList();

        } catch (final RuntimeException exception) {
            if (log.isErrorEnabled()) {
                log.error("An error occured when obtaining mailing alert list", exception);
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return new ArrayList<>();
    }
}
