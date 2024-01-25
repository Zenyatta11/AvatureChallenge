package com.zenyatta.challenge.avature.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@Entity
@RequiredArgsConstructor
@Table(name = "skills", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Skill extends BaseEntity {
    public Skill(final String name) {
        super(null, name);
    }
}
