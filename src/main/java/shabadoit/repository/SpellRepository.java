package shabadoit.repository;

import org.springframework.data.mongodb.repository.Query;
        import shabadoit.model.character.CharacterClass;
        import shabadoit.model.spell.Spell;
        import org.springframework.data.mongodb.repository.MongoRepository;
        import shabadoit.model.spell.SpellLevel;

import java.util.List;

public interface SpellRepository extends MongoRepository<Spell, String> {

    @Query("{'name': {$regex : ?0, $options: 'i'}}")
    List<Spell> findByNameQuery(String userName);
    Spell getByName(String name);
    void deleteById(String id);
    List<Spell> findBySpellLevel(SpellLevel spellLevel);
    List<Spell> findBySpellClasses(CharacterClass spellClasses);
    List<Spell> findBySpellClassesAndSpellLevel(CharacterClass spellClasses, SpellLevel spellLevel);
}
