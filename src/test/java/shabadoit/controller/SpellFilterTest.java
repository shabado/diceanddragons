package shabadoit.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import shabadoit.model.character.CharacterClass;
import shabadoit.model.spell.SpellFilter;
import shabadoit.model.spell.Spell;
import shabadoit.model.spell.SpellLevel;
import shabadoit.service.SpellService;
import shabadoit.service.impl.SpellFilterService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class SpellFilterTest {

    @Mock
    private SpellService spellService;

    private SpellFilterService spellFilterService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        spellFilterService = new SpellFilterService(spellService);
    }

    @Test
    public void should_filter_by_name() {
        String filterName = "name";
        Spell spell = new Spell(SpellLevel.LEVEL1, filterName);
        List<Spell> toAdd = Arrays.asList(spell);

        SpellFilter filter = SpellFilter.builder().name(filterName).build();

        when(spellService.searchByName(filterName)).thenReturn(toAdd);
        List<Spell> returnedSpells = spellFilterService.filterSpells(filter);

        verify(spellService).searchByName(filterName);
        assertEquals(toAdd, returnedSpells);
    }

    @Test
    public void should_filter_by_class() {
        CharacterClass charClass = CharacterClass.WIZARD;
        Spell spell = new Spell(SpellLevel.CANTRIP, "name");
        spell.setSpellClasses(Arrays.asList(charClass));
        List<Spell> toAdd = Arrays.asList(spell);

        SpellFilter filter = SpellFilter.builder().characterClass(charClass).build();

        when(spellService.listByClass(charClass)).thenReturn(toAdd);
        List<Spell> returnedSpells = spellFilterService.filterSpells(filter);

        verify(spellService).listByClass(charClass);
        assertEquals(toAdd, returnedSpells);
    }
    @Test
    public void should_filter_by_level() {
        SpellLevel spellLevel = SpellLevel.LEVEL1;
        Spell spell = new Spell(spellLevel, "name");
        List<Spell> toAdd = Arrays.asList(spell);

        SpellFilter filter = SpellFilter.builder().spellLevel(spellLevel).build();

        when(spellService.listBySpellLevel(spellLevel)).thenReturn(toAdd);
        List<Spell> returnedSpells = spellFilterService.filterSpells(filter);

        verify(spellService).listBySpellLevel(spellLevel);
        assertEquals(toAdd, returnedSpells);
    }

    @Test
    public void should_filter_by_class_and_level() {
        CharacterClass charClass = CharacterClass.WIZARD;
        SpellLevel spellLevel = SpellLevel.LEVEL1;
        Spell spell = new Spell(spellLevel, "name");
        spell.setSpellClasses(Arrays.asList(charClass));
        List<Spell> toAdd = Arrays.asList(spell);

        SpellFilter filter = SpellFilter.builder().spellLevel(spellLevel).characterClass(charClass).build();

        when(spellService.listByClassAndLevel(charClass, spellLevel)).thenReturn(toAdd);
        List<Spell> returnedSpells = spellFilterService.filterSpells(filter);

        verify(spellService).listByClassAndLevel(charClass, spellLevel);
        assertEquals(toAdd, returnedSpells);
    }

    @Test
    public void should_filter_by_name_class_and_level() {
        CharacterClass charClass = CharacterClass.WIZARD;
        SpellLevel spellLevel = SpellLevel.LEVEL1;
        String filterName = "name";
        Spell spell = new Spell(spellLevel, filterName);
        spell.setSpellClasses(Arrays.asList(charClass));
        List<Spell> toAdd = Arrays.asList(spell);

        SpellFilter filter = SpellFilter.builder().spellLevel(spellLevel)
                .characterClass(charClass).name(filterName).build();

        when(spellService.searchByName(filterName)).thenReturn(toAdd);
        List<Spell> returnedSpells = spellFilterService.filterSpells(filter);

        verify(spellService).searchByName(filterName);
        assertEquals(toAdd, returnedSpells);
    }

    @Test
    public void should_return_all_when_filter_empty() {
        String firstId = "id1";
        String secondId = "id2";
        Spell firstSpell = new Spell(SpellLevel.CANTRIP, "Name1");
        Spell secondSpell = new Spell(SpellLevel.LEVEL1, "Name2");
        firstSpell.setId(firstId);
        secondSpell.setId(secondId);

        List<Spell> existingSpells = new ArrayList<>(Arrays.asList(firstSpell, secondSpell));

        SpellFilter filter = SpellFilter.builder().build();

        when(spellService.listAllSpells()).thenReturn(existingSpells);
        List<Spell> returnedSpells = spellFilterService.filterSpells(filter);

        verify(spellService).listAllSpells();
        assertEquals(returnedSpells, existingSpells);
    }
}
