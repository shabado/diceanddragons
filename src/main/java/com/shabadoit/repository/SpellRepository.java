package com.shabadoit.repository;

import com.shabadoit.model.spell.Spell;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpellRepository extends MongoRepository<Spell, String> {

    List<Spell> findByName(@Param("name") String name);
    void deleteById(@Param("id") String id);
}
