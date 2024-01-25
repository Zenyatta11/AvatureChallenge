package com.zenyatta.challenge.avature.repository;

import com.zenyatta.challenge.avature.model.Skill;

public interface SkillRepository extends BaseEntityRepository<Skill> {
    @Override
    Skill findByName(String name);
}
