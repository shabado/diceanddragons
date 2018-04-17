package shabadoit.com;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import shabadoit.com.controller.SpellController;
import shabadoit.com.exceptions.SpellManagementException;
import shabadoit.com.model.character.CharacterClass;
import shabadoit.com.model.filter.SpellFilter;
import shabadoit.com.model.spell.Spell;
import shabadoit.com.model.spell.SpellLevel;
import shabadoit.com.repository.SpellRepository;
import shabadoit.com.service.impl.SpellFilterService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataMongoTest(includeFilters = @Filter(classes = {Service.class, Controller.class}))
public class SpellsMongoIT {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private SpellRepository spellRepository;

    @Autowired
    private SpellController spellController;

    private List<Spell> spells;
    private final String firstName = "name1";
    private final String secondName = "name2";
    private Spell firstSpell;
    private Spell secondSpell;


    @Before
    public void setUp() {
        spells = new ArrayList<>(Arrays.asList(new Spell(SpellLevel.LEVEL1, firstName), new Spell(SpellLevel.LEVEL1, secondName)));
        List<Spell> insertedSpells = spellRepository.insert(spells);
        firstSpell = insertedSpells.get(0);
        secondSpell = insertedSpells.get(1);
    }

    @After
    public void tearDown() {
        spellRepository.deleteAll();
    }

    @Test
    public void should_return_all_spells_in_db() {
        List<Spell> foundSpells = spellController.list().getBody();

        assertEquals(foundSpells, spells);
    }

    @Test
    public void should_return_spell_by_Id() {
        Spell spell = spellController.getById(firstSpell.getId()).getBody();

        assertEquals(firstSpell, spell);
    }

    @Test
    public void should_return_spells_by_level() {
        //This test relies on these being the only level 6 spells
        List<Spell> level6Spells = new ArrayList<>(Arrays.asList(new Spell(SpellLevel.LEVEL6, "Level6Spell1"),
                new Spell(SpellLevel.LEVEL6, "Level6Spell2")));
        spellRepository.insert(level6Spells);

        SpellFilter filter = SpellFilter.builder().spellLevel(SpellLevel.LEVEL6).build();

        List<Spell> foundSpells = spellController.filterSpells(filter).getBody();

        assertTrue(foundSpells.stream().allMatch(x -> x.getSpellLevel() == SpellLevel.LEVEL6));
        assertEquals(foundSpells, level6Spells);
    }

    @Test
    public void should_return_spells_by_class() {
        {
            CharacterClass classToSearch = CharacterClass.WIZARD;
            Spell filteredOutSpell = new Spell(SpellLevel.LEVEL1, "filtered");
            filteredOutSpell.setSpellClasses(Arrays.asList(CharacterClass.BARD));
            Spell returnedSingleclass = new Spell(SpellLevel.LEVEL1, "returned1");
            returnedSingleclass.setSpellClasses(Arrays.asList(classToSearch));
            Spell returnedMultiClass = new Spell(SpellLevel.LEVEL1, "returned2");
            returnedMultiClass.setSpellClasses(Arrays.asList(classToSearch, CharacterClass.BARD, CharacterClass.WARLOCK));

            List<Spell> spells = Arrays.asList(filteredOutSpell, returnedSingleclass, returnedMultiClass);
            spellRepository.insert(spells);

            SpellFilter filter = SpellFilter.builder().characterClass(classToSearch).build();

            List<Spell> foundSpells = spellController.filterSpells(filter).getBody();

            assertEquals(2, foundSpells.size());
            assertTrue(foundSpells.stream().allMatch(x -> x.getSpellClasses().contains(classToSearch)));
        }
    }

    @Test
    public void should_return_spells_by_class_and_level() {
        CharacterClass classToSearch = CharacterClass.WIZARD;
        SpellLevel levelToSearch = SpellLevel.CANTRIP;
        Spell filteredOutByClass = new Spell(levelToSearch, "filtered1");
        filteredOutByClass.setSpellClasses(Arrays.asList(CharacterClass.BARD));
        Spell filteredOutByLevel = new Spell(SpellLevel.LEVEL1, "filtered2");
        filteredOutByLevel.setSpellClasses(Arrays.asList(classToSearch));
        Spell returnedSingleclass = new Spell(levelToSearch, "returned1");
        returnedSingleclass.setSpellClasses(Arrays.asList(classToSearch));
        Spell returnedMultiClass = new Spell(levelToSearch, "returned2");
        returnedMultiClass.setSpellClasses(Arrays.asList(classToSearch, CharacterClass.BARD, CharacterClass.WARLOCK));

        List<Spell> spells = Arrays.asList(filteredOutByClass, filteredOutByLevel, returnedSingleclass, returnedMultiClass);
        spellRepository.insert(spells);

        SpellFilter filter = SpellFilter.builder().characterClass(classToSearch).spellLevel(levelToSearch).build();

        List<Spell> foundSpells = spellController.filterSpells(filter).getBody();

        assertEquals(2, foundSpells.size());
        assertTrue(foundSpells.stream().allMatch(x -> x.getSpellClasses().contains(classToSearch)));
        assertTrue(foundSpells.stream().allMatch(x -> x.getSpellLevel().equals(levelToSearch)));
    }


    @Test
    public void should_return_spell_by_name() {
        SpellFilter filter = SpellFilter.builder().name(firstName).build();

        List<Spell> filteredSpells = spellController.filterSpells(filter).getBody();

        assertEquals(1, filteredSpells.size());
        assertEquals(firstName, filteredSpells.get(0).getName());
    }

    @Test
    public void should_return_case_insensitive_by_name() {
        SpellFilter lowerCaseFilter = SpellFilter.builder().name(firstName.toLowerCase()).build();
        SpellFilter upperCaseFilter = SpellFilter.builder().name(firstName.toUpperCase()).build();

        List<Spell> filteredLowerCase = spellController.filterSpells(lowerCaseFilter).getBody();
        List<Spell> filteredUpperCase = spellController.filterSpells(upperCaseFilter).getBody();

        assertEquals(1, filteredLowerCase.size());
        assertEquals(firstName, filteredLowerCase.get(0).getName());

        assertEquals(1, filteredUpperCase.size());
        assertEquals(firstName, filteredUpperCase.get(0).getName());

        assertEquals(filteredLowerCase, filteredUpperCase);
    }

    @Test
    public void should_return_partial_match_by_name() {
        String partialName = "Partial";
        String fullName = partialName + "extraChars";
        Spell spell = new Spell(SpellLevel.LEVEL1, fullName);
        spellRepository.insert(spell);

        SpellFilter filter = SpellFilter.builder().name(partialName).build();

        List<Spell> filteredSpells = spellController.filterSpells(filter).getBody();

        assertEquals(1, filteredSpells.size());
        assertEquals(fullName, filteredSpells.get(0).getName());
    }

    @Test
    public void should_return_by_name_class_and_level() {
        String filterName = "filtered";
        CharacterClass classToSearch = CharacterClass.WIZARD;
        SpellLevel levelToSearch = SpellLevel.CANTRIP;
        Spell returnedSpell = new Spell(levelToSearch, filterName);
        returnedSpell.setSpellClasses(Arrays.asList(classToSearch));
        Spell returnedMultiClass = new Spell(levelToSearch, filterName + "1");
        returnedMultiClass.setSpellClasses(Arrays.asList(classToSearch, CharacterClass.BARD));
        Spell filteredOutByLevel = new Spell(SpellLevel.LEVEL1, filterName + "2");
        Spell filteredOutByName = new Spell(levelToSearch, "wrongname");
        Spell filteredOutByClass = new Spell(levelToSearch, filterName + "3");
        filteredOutByClass.setSpellClasses(Arrays.asList(CharacterClass.BARD));

        List<Spell> spells = Arrays.asList(returnedSpell, returnedMultiClass,
                filteredOutByClass, filteredOutByLevel, filteredOutByName);
        spellRepository.insert(spells);

        SpellFilter filter = SpellFilter.builder()
                .characterClass(classToSearch).spellLevel(levelToSearch).name(filterName).build();

        List<Spell> foundSpells = spellController.filterSpells(filter).getBody();

        assertEquals(foundSpells.size(), 2);
        assertTrue(foundSpells.stream().allMatch(x -> x.getSpellClasses().contains(classToSearch)));
        assertTrue(foundSpells.stream().allMatch(x -> x.getSpellLevel().equals(levelToSearch)));
        assertTrue(foundSpells.stream().allMatch(x -> x.getName().contains(filterName)));
    }

    @Test
    public void should_not_return_non_matching_by_name() {
    }

    @Test
    public void should_delete_spell() {
        spellController.delete(firstSpell.getId());

        List<Spell> expectedSpells = new ArrayList<>(Arrays.asList(secondSpell));

        assertEquals(expectedSpells, spellController.list().getBody());
    }

    @Test
    public void should_update_spell() {
        Spell updatedSpell = new Spell(firstSpell.getSpellLevel(), firstSpell.getName());
        updatedSpell.setName("New name");
        updatedSpell.setConcentration(true);
        updatedSpell.setId(firstSpell.getId());

        spellController.update(firstSpell.getId(), updatedSpell);

        assertEquals(updatedSpell, spellController.getById(firstSpell.getId()).getBody());
    }

    @Test()
    public void should_throw_when_Ids_differ() {
        exception.expect(SpellManagementException.class);
        spellController.update(secondSpell.getId(), firstSpell);
    }

    @Test()
    public void should_throw_if_adding_with_same_name() {
        exception.expect(SpellManagementException.class);
        spellController.create(firstSpell);
    }
}