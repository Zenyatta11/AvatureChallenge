package com.zenyatta.challenge.avature.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseEntityRepository<T> extends CrudRepository<T, Long> {
    T findByName(String name);
}
