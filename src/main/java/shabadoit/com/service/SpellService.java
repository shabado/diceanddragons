package shabadoit.com.service;

import shabadoit.com.model.character.CharacterClass;
import shabadoit.com.model.spell.Spell;
import shabadoit.com.model.spell.SpellLevel;

import java.util.List;

public interface SpellService {
    Spell addSpell(final Spell spellDetails);
    List<Spell> getAllSpells();
    Spell getByName(String name);
    Spell updateById(String id, Spell spell);
    void deleteById(String id);
    Spell getById(String id);
    List<Spell> listByClass(CharacterClass characterClass);
    List<Spell> listBySpellLevel(SpellLevel spellLevel);
    List<Spell> listByClassAndLevel(CharacterClass characterClass, SpellLevel spellLevel);
}
