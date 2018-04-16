package shabadoit.com.controller;

import shabadoit.com.model.character.CharacterClass;
import shabadoit.com.model.spell.SpellLevel;
import shabadoit.com.service.SpellService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shabadoit.com.model.spell.Spell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SpellControllerTest {

    @InjectMocks
    private SpellController spellController;

    @Mock
    private SpellService spellService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_get_spell_by_id() {
        String id = "Id";
        Spell existingSpell = new Spell(SpellLevel.LEVEL1, "Name");
        existingSpell.setId(id);

        when(spellService.getById(id)).thenReturn(existingSpell);
        Spell spell = spellController.getById(id);

        verify(spellService).getById(id);
        assertThat(spell.getId(), is(id));
    }

    @Test
    public void should_list_all_spells() {
        String firstId = "id1";
        String secondId = "id2";
        Spell firstSpell = new Spell(SpellLevel.CANTRIP, "Name1");
        Spell secondSpell = new Spell(SpellLevel.LEVEL1, "Name2");
        firstSpell.setId(firstId);
        secondSpell.setId(secondId);

        List<Spell> existingSpells = new ArrayList<>(Arrays.asList(firstSpell, secondSpell));

        when(spellService.getAllSpells()).thenReturn(existingSpells);
        List<Spell> spells = spellController.list();

        verify(spellService).getAllSpells();
        assertEquals(spells, existingSpells);
    }

    @Test
    public void should_call_add_new_spell() {
        Spell spellToAdd = new Spell(SpellLevel.CANTRIP, "Name1");

        when(spellService.addSpell(spellToAdd)).thenReturn(spellToAdd);
        Spell created = spellController.create(spellToAdd);

        verify(spellService).addSpell(spellToAdd);
        assertEquals(created, spellToAdd);
    }

    @Test
    public void should_get_by_name() {
        String name = "name";
        Spell spell = new Spell(SpellLevel.CANTRIP, name);

        when(spellService.getByName(name)).thenReturn(spell);
        Spell returnedSpell = spellController.getByName(name);

        verify(spellService).getByName(name);
        assertEquals(spell, returnedSpell);
    }

    @Test
    public void should_call_update() {
        String id = "id";
        Spell spellToUpdate = new Spell(SpellLevel.CANTRIP, "Name1");

        when(spellService.updateById(id, spellToUpdate)).thenReturn(spellToUpdate);
        Spell updated = spellController.update(id, spellToUpdate);

        verify(spellService).updateById(id, spellToUpdate);
        assertEquals(updated, spellToUpdate);
    }

    @Test
    public void should_delete_call_delete() {
        String id = "id";

        spellController.delete(id);

        verify(spellService).deleteById(id);
    }

    @Test
    public void should_get_by_class() {
        CharacterClass charClass = CharacterClass.WIZARD;
        Spell spell = new Spell(SpellLevel.CANTRIP, "name");
        spell.setSpellClasses(Arrays.asList(charClass));
        List<Spell> toAdd = Arrays.asList(spell);

        when(spellService.listByClass(charClass)).thenReturn(toAdd);
        List<Spell> returnedSpells = spellController.listByClass(charClass);

        verify(spellService).listByClass(charClass);
        assertEquals(toAdd, returnedSpells);
    }

    @Test
    public void should_get_by_level() {
        SpellLevel spellLevel = SpellLevel.LEVEL1;
        Spell spell = new Spell(spellLevel, "name");
        List<Spell> toAdd = Arrays.asList(spell);

        when(spellService.listBySpellLevel(spellLevel)).thenReturn(toAdd);
        List<Spell> returnedSpells = spellController.listByLevel(spellLevel);

        verify(spellService).listBySpellLevel(spellLevel);
        assertEquals(toAdd, returnedSpells);
    }

    @Test
    public void should_get_by_class_and_level() {
        CharacterClass charClass = CharacterClass.WIZARD;
        SpellLevel spellLevel = SpellLevel.LEVEL1;
        Spell spell = new Spell(spellLevel, "name");
        spell.setSpellClasses(Arrays.asList(charClass));
        List<Spell> toAdd = Arrays.asList(spell);

        when(spellService.listByClassAndLevel(charClass, spellLevel)).thenReturn(toAdd);
        List<Spell> returnedSpells = spellController.listByClassAndLevel(charClass, spellLevel);

        verify(spellService).listByClassAndLevel(charClass, spellLevel);
        assertEquals(toAdd, returnedSpells);
    }
}
