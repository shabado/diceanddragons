package shabadoit.com.repository;

import shabadoit.com.model.spell.Spell;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpellRepository extends MongoRepository<Spell, String> {

    Spell findByName(@Param("name") String name);
    void deleteById(@Param("id") String id);
}
