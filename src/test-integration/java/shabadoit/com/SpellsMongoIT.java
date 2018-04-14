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
import shabadoit.com.model.spell.Spell;
import shabadoit.com.model.spell.SpellLevel;
import shabadoit.com.repository.SpellRepository;

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
        List<Spell> foundSpells = spellController.list();

        assertEquals(foundSpells, spells);
    }

    @Test
    public void should_return_spell_by_Id() {
        Spell spell = spellController.getById(firstSpell.getId());

        assertEquals(firstSpell, spell);
    }

    @Test
    public void should_return_spells_by_level() {
        //This test relies on these being the only level 6 spells
        List<Spell> level6Spells = new ArrayList<>(Arrays.asList(new Spell(SpellLevel.LEVEL6, "Level6Spell1"),
                new Spell(SpellLevel.LEVEL6, "Level6Spell2")));
        spellRepository.insert(level6Spells);

        List<Spell> foundSpells = spellController.listByLevel(SpellLevel.LEVEL6);

        assertTrue(foundSpells.stream().allMatch(x -> x.getSpellLevel() == SpellLevel.LEVEL6));
        assertEquals(foundSpells, level6Spells);
    }

    @Test
    public void should_return_spells_by_class() {
        {
            //TODO tidy up
            Spell spell1 = new Spell(SpellLevel.LEVEL1, "bardspell");
            spell1.setSpellClasses(Arrays.asList(CharacterClass.BARD));
            Spell spell2 = new Spell(SpellLevel.LEVEL1, "wizardspell");
            spell2.setSpellClasses(Arrays.asList(CharacterClass.WIZARD));
            Spell spell3 = new Spell(SpellLevel.LEVEL1, "wizardspell2");
            spell3.setSpellClasses(Arrays.asList(CharacterClass.WIZARD, CharacterClass.BARD, CharacterClass.WARLOCK));

            spellRepository.insert(spell1);
            spellRepository.insert(spell2);
            spellRepository.insert(spell3);

            List<Spell> foundSpells = spellController.listByClass(CharacterClass.WIZARD);
            List<Spell> foundSpells2 = spellController.listByClassAndLevel(CharacterClass.WIZARD, SpellLevel.LEVEL1);

            assertTrue(foundSpells.stream().allMatch(x -> x.getSpellClasses().contains(CharacterClass.WIZARD)));

        }
    }

    @Test
    public void should_return_spells_by_class_and_level() {
        //TODO todo
        assertTrue(false);
    }



    @Test
    public void should_return_spell_by_name() {
        Spell spell = spellController.getByName(firstName);

        assertEquals(firstSpell, spell);
    }

    @Test
    public void should_delete_spell() {
        spellController.delete(firstSpell.getId());

        List<Spell> expectedSpells = new ArrayList<>(Arrays.asList(secondSpell));

        assertNull(spellController.getById(firstSpell.getId()));
        assertEquals(expectedSpells, spellController.list());
    }

    @Test
    public void should_update_spell() {
        Spell updatedSpell = new Spell(firstSpell.getSpellLevel(), firstSpell.getName());
        updatedSpell.setName("New name");
        updatedSpell.setConcentration(true);
        updatedSpell.setId(firstSpell.getId());

        spellController.update(firstSpell.getId(), updatedSpell);

        assertEquals(updatedSpell, spellController.getById(firstSpell.getId()));
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