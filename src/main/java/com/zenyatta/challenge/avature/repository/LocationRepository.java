package com.zenyatta.challenge.avature.repository;

import com.zenyatta.challenge.avature.model.Location;

public interface LocationRepository extends BaseEntityRepository<Location> {
    @Override
    Location findByName(String name);
}
