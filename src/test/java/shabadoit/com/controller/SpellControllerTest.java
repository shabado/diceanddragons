package shabadoit.com.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import shabadoit.com.model.character.CharacterClass;
import shabadoit.com.model.filter.SpellFilter;
import shabadoit.com.model.spell.Spell;
import shabadoit.com.model.spell.SpellLevel;
import shabadoit.com.service.SpellService;
import shabadoit.com.service.impl.SpellFilterService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class SpellControllerTest {
    private SpellController spellController;

    @Mock
    private SpellService spellService;

    @Mock
    private SpellFilterService spellFilterService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        spellController = new SpellController(spellService, spellFilterService);
    }

    @Test
    public void should_get_spell_by_id() {
        String id = "Id";
        Spell existingSpell = new Spell(SpellLevel.LEVEL1, "Name");
        existingSpell.setId(id);

        when(spellService.getById(id)).thenReturn(Optional.of(existingSpell));
        Spell spell = spellController.getById(id).getBody();

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

        when(spellService.listAllSpells()).thenReturn(existingSpells);
        List<Spell> spells = spellController.list().getBody();

        verify(spellService).listAllSpells();
        assertEquals(spells, existingSpells);
    }

    @Test
    public void should_call_add_new_spell() {
        Spell spellToAdd = new Spell(SpellLevel.CANTRIP, "Name1");

        when(spellService.addSpell(spellToAdd)).thenReturn(spellToAdd);
        Spell created = spellController.create(spellToAdd).getBody();

        verify(spellService).addSpell(spellToAdd);
        assertEquals(created, spellToAdd);
    }

    @Test
    public void should_call_update() {
        String id = "id";
        Spell spellToUpdate = new Spell(SpellLevel.CANTRIP, "Name1");

        when(spellService.updateById(id, spellToUpdate)).thenReturn(spellToUpdate);
        Spell updated = spellController.update(id, spellToUpdate).getBody();

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
    public void should_call_filter() {
        SpellFilter filter = SpellFilter.builder().build();

        SpellLevel spellLevel = SpellLevel.LEVEL1;
        Spell spell = new Spell(spellLevel, "name");
        List<Spell> toAdd = Arrays.asList(spell);


        when(spellFilterService.filterSpells(filter)).thenReturn(toAdd);
        List<Spell> returnedSpells = spellController.filterSpells(filter).getBody();

        verify(spellFilterService).filterSpells(filter);
        assertEquals(toAdd, returnedSpells);
    }
}
