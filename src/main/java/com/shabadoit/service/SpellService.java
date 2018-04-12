package com.shabadoit.service;

import com.shabadoit.model.spell.Spell;

import java.util.List;
import java.util.Optional;

public interface SpellService {
    void addSpell(final Spell spellDetails);
    List<Spell> getAllSpells();
    Spell getByName(String name);
    void updateById(String id, Spell spell);
    void deleteById(String id);
    Spell getById(String id);
}
