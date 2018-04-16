package shabadoit.com.repository;

import shabadoit.com.model.character.CharacterClass;
import shabadoit.com.model.spell.Spell;
import org.springframework.data.mongodb.repository.MongoRepository;
import shabadoit.com.model.spell.SpellLevel;

import java.util.List;

public interface SpellRepository extends MongoRepository<Spell, String> {

    Spell findByName(String name);
    void deleteById(String id);
    List<Spell> findBySpellLevel(SpellLevel spellLevel);
    List<Spell> findBySpellClasses(CharacterClass spellClasses);
    List<Spell> findBySpellClassesAndSpellLevel(CharacterClass spellClasses, SpellLevel spellLevel);
}
