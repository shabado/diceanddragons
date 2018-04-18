package shabadoit.service;

import shabadoit.model.character.CharacterClass;
import shabadoit.model.spell.Spell;
import shabadoit.model.spell.SpellLevel;

import java.util.List;
import java.util.Optional;

public interface SpellService {
    Spell addSpell(final Spell spellDetails);
    List<Spell> listAllSpells();
    List<Spell> searchByName(String name);
    Spell getByName(String name);
    Spell updateById(String id, Spell spell);
    void deleteById(String id);
    Optional<Spell> getById(String id);
    List<Spell> listByClass(CharacterClass characterClass);
    List<Spell> listBySpellLevel(SpellLevel spellLevel);
    List<Spell> listByClassAndLevel(CharacterClass characterClass, SpellLevel spellLevel);
}
