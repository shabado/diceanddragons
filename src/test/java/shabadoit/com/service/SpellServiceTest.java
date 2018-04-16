package shabadoit.com.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shabadoit.com.exceptions.SpellManagementException;
import shabadoit.com.model.character.CharacterClass;
import shabadoit.com.model.spell.Spell;
import shabadoit.com.model.spell.SpellLevel;
import shabadoit.com.repository.SpellRepository;
import shabadoit.com.service.impl.SpellServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SpellServiceTest {
    @InjectMocks
    private SpellServiceImpl spellService;

    @Mock
    private SpellRepository spellRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void should_add_spell() {
        Spell spell = new Spell(SpellLevel.LEVEL1, "Name");
        Spell spellWithId = new Spell(spell.getSpellLevel(), spell.getName());
        spellWithId.setId("id");

        when(spellRepository.insert(spell)).thenReturn(spellWithId);
        Spell returnedSpell = spellService.addSpell(spell);

        verify(spellRepository).insert(spell);
        assertEquals(returnedSpell, spellWithId);
    }

    @Test
    public void should_get_all_spells() {
        String firstId = "id1";
        String secondId = "id2";
        Spell firstSpell = new Spell(SpellLevel.CANTRIP, "Name1");
        Spell secondSpell = new Spell(SpellLevel.LEVEL1, "Name2");
        firstSpell.setId(firstId);
        secondSpell.setId(secondId);
        List<Spell> existingSpells = new ArrayList<>(Arrays.asList(firstSpell, secondSpell));

        when(spellRepository.findAll()).thenReturn(existingSpells);
        List<Spell> returnedSpells = spellService.getAllSpells();

        verify(spellRepository).findAll();
        assertEquals(existingSpells, returnedSpells);
    }

    @Test
    public void should_get_by_name() {
        Spell dbSpell = new Spell(SpellLevel.LEVEL1, "Name2");
        dbSpell.setId("id");

        when(spellRepository.insert(dbSpell)).thenReturn(dbSpell);
        Spell returnedSpell = spellService.addSpell(dbSpell);

        verify(spellRepository).insert(dbSpell);
        assertEquals(dbSpell, returnedSpell);
    }

    @Test
    public void should_update_by_id_if_exists() {
        String id = "id";
        Spell dbSpell = new Spell(SpellLevel.LEVEL1, "Name2");
        dbSpell.setId(id);

        when(spellRepository.findById(id)).thenReturn(Optional.of(dbSpell));
        when(spellRepository.save(dbSpell)).thenReturn(dbSpell);
        Spell returnedSpell = spellService.updateById(id, dbSpell);

        verify(spellRepository).findById(id);
        verify(spellRepository).save(dbSpell);
        assertEquals(dbSpell, returnedSpell);
    }

    @Test
    public void should_delete_by_id_if_exists() {
        String id = "id";
        Spell dbSpell = new Spell(SpellLevel.LEVEL1, "Name2");
        dbSpell.setId(id);

        when(spellRepository.findById(id)).thenReturn(Optional.of(dbSpell));
        spellService.deleteById(id);

        verify(spellRepository).findById(id);
        verify(spellRepository).deleteById(id);
    }

    @Test
    public void should_get_by_id() {
        String id = "id";
        Spell dbSpell = new Spell(SpellLevel.LEVEL1, "Name2");
        dbSpell.setId(id);

        when(spellRepository.findById(id)).thenReturn(Optional.of(dbSpell));

        Spell returnedSpell = spellService.getById(id);
        assertEquals(dbSpell, returnedSpell);
    }

    @Test
    public void should_list_by_class() {
        CharacterClass spellClass = CharacterClass.WIZARD;
        String id = "id";
        Spell dbSpell = new Spell(SpellLevel.LEVEL1, "Name2");
        dbSpell.setId(id);
        dbSpell.setSpellClasses(Arrays.asList(spellClass));
        List<Spell> dbSpells = Arrays.asList(dbSpell);

        when(spellRepository.findBySpellClasses(spellClass)).thenReturn(dbSpells);
        List<Spell> returnedSpells = spellService.listByClass(spellClass);

        verify(spellRepository).findBySpellClasses(spellClass);
        assertEquals(dbSpells, returnedSpells);
    }

    @Test
    public void should_list_by_level() {
        SpellLevel spellLevel = SpellLevel.CANTRIP;
        String id = "id";
        Spell dbSpell = new Spell(spellLevel, "Name2");
        dbSpell.setId(id);
        List<Spell> dbSpells = Arrays.asList(dbSpell);

        when(spellRepository.findBySpellLevel(spellLevel)).thenReturn(dbSpells);
        List<Spell> returnedSpells = spellService.listBySpellLevel(spellLevel);

        verify(spellRepository).findBySpellLevel(spellLevel);
        assertEquals(dbSpells, returnedSpells);
    }

    @Test
    public void should_list_by_class_and_level() {
        SpellLevel spellLevel = SpellLevel.CANTRIP;
        CharacterClass spellClass = CharacterClass.WIZARD;
        String id = "id";
        Spell dbSpell = new Spell(spellLevel, "Name2");
        dbSpell.setId(id);
        dbSpell.setSpellClasses(Arrays.asList(spellClass));
        List<Spell> dbSpells = Arrays.asList(dbSpell);

        when(spellRepository.findBySpellClassesAndSpellLevel(spellClass, spellLevel)).thenReturn(dbSpells);
        List<Spell> returnedSpells = spellService.listByClassAndLevel(spellClass, spellLevel);

        verify(spellRepository).findBySpellClassesAndSpellLevel(spellClass, spellLevel);
        assertEquals(dbSpells, returnedSpells);
    }

    @Test
    public void should_throw_if_delete_by_non_existent_id() {
        String id = "id";

        when(spellRepository.findById(id)).thenReturn(Optional.empty());

        exception.expect(SpellManagementException.class);
        exception.expectMessage("Spell with Id " + id + " does not exist");
        spellService.deleteById(id);
    }

    @Test
    public void should_throw_if_update_id_does_not_match() {
        String id = "id";
        String dbSpellId = "noMatch";
        Spell dbSpell = new Spell(SpellLevel.LEVEL1, "Name2");
        dbSpell.setId(dbSpellId);

        when(spellRepository.findById(id)).thenReturn(Optional.of(dbSpell));

        exception.expect(SpellManagementException.class);
        exception.expectMessage("Supplied spell Ids do not match, unable to update");
        spellService.updateById(id, dbSpell);
    }

    @Test
    public void should_throw_if_same_name_added() {
        String id = "id";
        String name = "name";
        Spell dbSpell = new Spell(SpellLevel.LEVEL1, name);
        dbSpell.setId(id);

        when(spellRepository.findByName(name)).thenReturn(dbSpell);

        exception.expect(SpellManagementException.class);
        exception.expectMessage("Spell with name " + name + " already exists");
        spellService.addSpell(new Spell(SpellLevel.LEVEL1, name));
    }

    @Test
    public void should_throw_if_same_id_added() {
        String id = "id";
        Spell dbSpell = new Spell(SpellLevel.LEVEL1, "name");
        dbSpell.setId(id);
        Spell spellToAdd = new Spell(SpellLevel.LEVEL1, "diffName");
        spellToAdd.setId(id);

        when(spellRepository.findById(id)).thenReturn(Optional.of(dbSpell));

        exception.expect(SpellManagementException.class);
        exception.expectMessage("Spell with Id " + id + " already exists");
        spellService.addSpell(dbSpell);
    }

    @Test
    public void should_return_null_if_get_by_non_existent_id() {
        String id = "notFoundId";

        when(spellRepository.findById(id)).thenReturn(Optional.empty());

        assertNull(spellService.getById(id));
    }
}
