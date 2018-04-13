package shabadoit.com.service;

import shabadoit.com.model.spell.Spell;

import java.util.List;

public interface SpellService {
    Spell addSpell(final Spell spellDetails);
    List<Spell> getAllSpells();
    Spell getByName(String name);
    Spell updateById(String id, Spell spell);
    void deleteById(String id);
    Spell getById(String id);
}
