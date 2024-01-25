package com.zenyatta.challenge.avature.repository;

import com.zenyatta.challenge.avature.model.Alert;
import org.springframework.data.repository.CrudRepository;

public interface AlertRepository extends CrudRepository<Alert, Long> {
    Alert findByEmail(String email);
}
