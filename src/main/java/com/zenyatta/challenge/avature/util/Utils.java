package com.zenyatta.challenge.avature.util;

import com.zenyatta.challenge.avature.model.BaseEntity;
import com.zenyatta.challenge.avature.repository.BaseEntityRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Utils {

    private Utils() {
    }

    public static <T extends BaseEntity> Set<T> listOfStringsToSet(final List<String> names, final Class<T> entityType) {
        final Set<T> entities = new HashSet<>();

        for (final String name : names) {
            T entity;

            try {
                entity = entityType.getDeclaredConstructor(String.class).newInstance(name);
            } catch (final Exception exception) {
                throw new RuntimeException("Failed to create entity from name", exception);
            }

            entities.add(entity);
        }

        return entities;
    }

    
    public static <T extends BaseEntity> Set<T> processEntitySet(final Set<T> entitySet,
            final BaseEntityRepository<T> repository) {
        if (entitySet == null || entitySet.isEmpty()) {
            return new HashSet<T>();
        }

        final Set<T> processedEntities = new HashSet<>();

        for (final T entity : entitySet) {
            final T existingEntry = repository.findByName(entity.getName());

            if (existingEntry != null) {
                processedEntities.add(existingEntry);
            } else {
                final T newEntry = repository.save(entity);
                processedEntities.add(newEntry);
            }
        }

        return processedEntities;
    }

    public static <T extends BaseEntity> T processEntitySet(final T entity, final BaseEntityRepository<T> repository) {
        if (entity == null) {
            return null;
        }

        final T existingEntry = repository.findByName(entity.getName());

        if (existingEntry == null) {
            return repository.save(entity);
        }

        return existingEntry;
    }

}
