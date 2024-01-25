package com.zenyatta.challenge.avature.service;

import static com.zenyatta.challenge.avature.util.Utils.processEntitySet;

import com.zenyatta.challenge.avature.exception.JobberException;
import com.zenyatta.challenge.avature.model.Alert;
import com.zenyatta.challenge.avature.repository.AlertRepository;
import com.zenyatta.challenge.avature.repository.LocationRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final LocationRepository locationRepository;
    private final AlertRepository repository;

    public Alert createAlert(final Alert alert) throws JobberException {
        final Alert existingAlert = repository.findByEmail(alert.getEmail());

        if (existingAlert != null) {
            existingAlert.setLocation(processEntitySet(alert.getLocation(), locationRepository));
            existingAlert.setMaxSalary(alert.getMaxSalary());
            existingAlert.setMinSalary(alert.getMinSalary());
            existingAlert.setTitle(alert.getTitle());

            return save(existingAlert.toBuilder().build());
        } else {
            return save(alert.toBuilder().build());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Alert save(@NotNull final Alert alert) {
        return repository.save(alert);
    }
}
