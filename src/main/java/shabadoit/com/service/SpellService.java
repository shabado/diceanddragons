package shabadoit.com.service;

import shabadoit.com.model.spell.Spell;

import java.util.List;

public interface SpellService {
    void addSpell(final Spell spellDetails);
    List<Spell> getAllSpells();
    Spell getByName(String name);
    void updateById(String id, Spell spell);
    void deleteById(String id);
    Spell getById(String id);
}
