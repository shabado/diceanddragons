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
import shabadoit.com.model.spell.Spell;
import shabadoit.com.model.spell.SpellLevel;
import shabadoit.com.repository.SpellRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataMongoTest (includeFilters = @Filter (classes = {Service.class, Controller.class}))
public class SpellsMongoIT {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private SpellRepository spellRepository;

    @Autowired
    private SpellController spellController;

    private List<Spell> spells;
    private final String firstId = "Id";
    private final String secondId = "Id2";
    private final String firstName = "name1";
    private final String secondName = "name2";
    private final Spell firstSpell;
    private final Spell secondSpell;

    public SpellsMongoIT() {
        firstSpell = new Spell(firstId, SpellLevel.LEVEL1, firstName);
        secondSpell = new Spell(secondId, SpellLevel.LEVEL1, secondName);
    }


    @Before
    public void setUp() {
        Spell firstSpell = new Spell(firstId, SpellLevel.LEVEL1, firstName);
        Spell secondSpell = new Spell(secondId, SpellLevel.LEVEL1, secondName);
        spells = new ArrayList<>(Arrays.asList(firstSpell, secondSpell));
        spellRepository.insert(spells);
    }

    @Test
    public void should_return_all_spells_in_db() {
        List<Spell> foundSpells = spellController.list();

        assertEquals(foundSpells, spells);
    }

    @Test
    public void should_return_spell_by_Id() {
        Spell spell = spellController.getById(firstId);

        assertEquals(firstSpell, spell);
    }

    @Test
    public void should_return_spell_by_name() {
        Spell spell = spellController.getByName(firstName);

        assertEquals(firstSpell, spell);
    }

    @Test
    public void should_delete_spell() {
        spellController.delete(firstId);

        List<Spell> expectedSpells = new ArrayList<>(Arrays.asList(secondSpell));

        assertNull(spellController.getById(firstId));
        assertEquals(expectedSpells, spellController.list());
    }

    @Test
    public void should_update_spell() {
        Spell updatedSpell = new Spell(firstSpell.getId(), firstSpell.getSpellLevel(), firstSpell.getName());
        updatedSpell.setName("New name");
        updatedSpell.setConcentration(true);

        spellController.update(firstId, updatedSpell);

        assertEquals(updatedSpell, spellController.getById(firstId));
    }

    @Test()
    public void should_500_when_Ids_differ() {
        exception.expect(SpellManagementException.class);
        spellController.update(secondId, firstSpell);
    }

    @Test()
    public void should_error_if_adding_with_same_name() {
        exception.expect(SpellManagementException.class);
        spellController.create(firstSpell);
    }

    @After
    public void tearDown() {
        spellRepository.deleteAll();
    }
}