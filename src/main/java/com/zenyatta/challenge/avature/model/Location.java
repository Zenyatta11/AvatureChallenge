package com.zenyatta.challenge.avature.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@Entity
@RequiredArgsConstructor
@Table(name = "locations", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Location extends BaseEntity {
    public Location(final String name) {
        super(null, name);
    }
}
