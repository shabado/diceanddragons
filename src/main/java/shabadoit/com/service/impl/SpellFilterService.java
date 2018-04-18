package shabadoit.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shabadoit.com.model.character.CharacterClass;
import shabadoit.com.model.spell.SpellFilter;
import shabadoit.com.model.spell.Spell;
import shabadoit.com.model.spell.SpellLevel;
import shabadoit.com.service.SpellService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpellFilterService {

    private SpellService spellService;

    @Autowired
    public SpellFilterService(final SpellService spellService) {
        this.spellService = spellService;
    }

    public List<Spell> filterSpells(SpellFilter spellFilter) {
        if (spellFilter.getName() == null) {
            return filterSpellList(spellFilter.getCharacterClass(), spellFilter.getSpellLevel());
        } else {
            return filterSpellList(spellFilter);
        }
    }

    private List<Spell> filterSpellList(SpellFilter spellFilter) {
        List<Spell> queriedSpells = spellService.searchByName(spellFilter.getName());
        if (spellFilter.getSpellLevel() != null && spellFilter.getCharacterClass() != null) {
            return queriedSpells.stream()
                    .filter(x -> x.getSpellClasses() != null && x.getSpellClasses().contains(spellFilter.getCharacterClass()))
                    .filter(x -> x.getSpellLevel() != null && x.getSpellLevel() == spellFilter.getSpellLevel())
                    .collect(Collectors.toList());
        } else if (spellFilter.getSpellLevel() != null) {
            return queriedSpells.stream()
                    .filter(x -> x.getSpellLevel() != null && x.getSpellLevel() == spellFilter.getSpellLevel())
                    .collect(Collectors.toList());
        } else if (spellFilter.getCharacterClass() != null) {
            return queriedSpells.stream()
                    .filter(x -> x.getSpellClasses() != null && x.getSpellClasses().contains(spellFilter.getCharacterClass()))
                    .collect(Collectors.toList());
        } else {
            return queriedSpells;
        }
    }

    private List<Spell> filterSpellList(CharacterClass characterClass, SpellLevel spellLevel) {
        if (spellLevel != null && characterClass != null) {
            return spellService.listByClassAndLevel(characterClass, spellLevel);
        } else if (spellLevel != null) {
            return spellService.listBySpellLevel(spellLevel);
        } else if (characterClass != null){
            return spellService.listByClass(characterClass);
        } else {
            return spellService.listAllSpells();
        }
    }
}
