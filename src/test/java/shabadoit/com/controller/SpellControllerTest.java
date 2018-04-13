package shabadoit.com.controller;

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
        //Given
        String id = "Id";
        Spell existingSpell = new Spell(SpellLevel.LEVEL1, "Name");
        existingSpell.setId(id);

        //When
        when(spellService.getById(id)).thenReturn(existingSpell);
        Spell spell = spellController.getById(id);

        //Then
        verify(spellService).getById(id);
        assertThat(spell.getId(), is(id));
    }

    @Test
    public void should_list_all_spells() {
        //Given
        String firstId = "id1";
        String secondId = "id2";
        Spell firstSpell = new Spell(SpellLevel.CANTRIP, "Name1");
        Spell secondSpell = new Spell(SpellLevel.LEVEL1, "Name2");
        firstSpell.setId(firstId);
        secondSpell.setId(secondId);

        List<Spell> existingSpells = new ArrayList<>(Arrays.asList(firstSpell, secondSpell));

        //When
        when(spellService.getAllSpells()).thenReturn(existingSpells);
        List<Spell> spells = spellController.list();

        //Then
        verify(spellService).getAllSpells();
        assertEquals(spells, existingSpells);
    }

    @Test
    public void should_create_new_spell() {}

    @Test
    public void should_get_by_name() {}

    @Test
    public void should_update() {}

    @Test
    public void should_not_update_when_ids_differ() {}

    @Test
    public void should_delete() {}
}
