package shabadoit.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import shabadoit.exceptions.CharacterManagementException;
import shabadoit.model.character.CharacterClass;
import shabadoit.model.character.CharacterSheet;
import shabadoit.model.character.ClassBlock;
import shabadoit.model.character.HitDie;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CharacterSheetTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void should_initialise_current_hp_to_max() {
        CharacterSheet character = new CharacterSheet();
        character.setMaxHP(50);

        assertEquals(character.getMaxHP(), character.getCurrentHP());
    }

    @Test
    public void should_alter_hp() {
        CharacterSheet character = new CharacterSheet();
        character.setMaxHP(50);
        character.alterHp(-7);

        assertEquals(43, character.getCurrentHP());
    }

    @Test
    public void should_not_alter_above_max() {
        CharacterSheet character = new CharacterSheet();
        character.setMaxHP(50);
        character.alterHp(254);

        assertEquals(character.getMaxHP(), character.getCurrentHP());
    }

    @Test
    public void should_not_alter_below_zero() {
        CharacterSheet character = new CharacterSheet();
        character.setMaxHP(50);
        character.alterHp(-70);

        assertEquals(0, character.getCurrentHP());
    }

    @Test
    public void should_increment_character_level_on_level_up() {
        ClassBlock classBlock = new ClassBlock(1);
        CharacterSheet character = new CharacterSheet();
        character.addClass(CharacterClass.BARD, classBlock);

        character.levelUp(CharacterClass.BARD);

        assertEquals(2, character.getCharacterLevel());
    }

    @Test
    public void should_add_new_class_when_level_up() {
        CharacterSheet character = new CharacterSheet();
        character.levelUp(CharacterClass.BARD);

        assertEquals(1, character.getCharacterLevel());
        assertTrue(character.getClasses().containsKey(CharacterClass.BARD));
    }

    @Test
    public void should_get_hit_die() {
        ClassBlock classBlock = new ClassBlock(3);
        CharacterSheet character = new CharacterSheet();
        character.addClass(CharacterClass.BARD, classBlock);

        Map<HitDie, Integer> hitDie = character.getTotalHitDice();

        assertEquals(3, hitDie.get(CharacterClass.BARD.getHitDie()).intValue());
    }

    @Test
    public void should_get_hit_die_for_multiclass_with_same_die() {
        ClassBlock classBlock = new ClassBlock(3);
        ClassBlock classBlockTwo = new ClassBlock(12);
        CharacterSheet character = new CharacterSheet();
        Map<CharacterClass, ClassBlock> classMap = new HashMap<>();
        classMap.put(CharacterClass.BARD, classBlock);
        classMap.put(CharacterClass.WARLOCK, classBlockTwo);

        character.setClasses(classMap);

        Map<HitDie, Integer> hitDie = character.getTotalHitDice();

        assertEquals(15, hitDie.get(CharacterClass.BARD.getHitDie()).intValue());
    }

    @Test
    public void should_get_hit_die_for_multiclass_with_different_die() {
        ClassBlock classBlock = new ClassBlock(3);
        ClassBlock classBlockTwo = new ClassBlock(12);
        CharacterSheet character = new CharacterSheet();
        Map<CharacterClass, ClassBlock> classMap = new HashMap<>();
        classMap.put(CharacterClass.BARD, classBlock);
        classMap.put(CharacterClass.FIGHTER, classBlockTwo);

        character.setClasses(classMap);

        Map<HitDie, Integer> hitDie = character.getTotalHitDice();

        assertEquals(3, hitDie.get(CharacterClass.BARD.getHitDie()).intValue());
        assertEquals(12, hitDie.get(CharacterClass.FIGHTER.getHitDie()).intValue());
    }


    @Test
    public void should_update_hit_die_on_level_up() {
        ClassBlock classBlock = new ClassBlock(3);
        CharacterSheet character = new CharacterSheet();
        character.addClass(CharacterClass.BARD, classBlock);

        character.levelUp(CharacterClass.BARD);

        Map<HitDie, Integer> hitDie = character.getTotalHitDice();

        assertEquals(4, hitDie.get(CharacterClass.BARD.getHitDie()).intValue());
    }

    @Test
    public void should_set_character_level() {
        ClassBlock classBlock = new ClassBlock(3);
        CharacterSheet character = new CharacterSheet();
        character.addClass(CharacterClass.BARD, classBlock);

        assertEquals(3, character.getCharacterLevel());
    }

    @Test
    public void should_set_character_level_multiple_classes() {
        ClassBlock classBlock = new ClassBlock(3);
        ClassBlock classBlockTwo = new ClassBlock(12);
        CharacterSheet character = new CharacterSheet();
        Map<CharacterClass, ClassBlock> classMap = new HashMap<>();
        classMap.put(CharacterClass.BARD, classBlock);
        classMap.put(CharacterClass.WARLOCK, classBlockTwo);

        character.setClasses(classMap);

        assertEquals(15, character.getCharacterLevel());
    }

    @Test
    public void should_throw_if_levelling_past_20() {
        ClassBlock classBlock = new ClassBlock(8);
        ClassBlock classBlockTwo = new ClassBlock(12);
        CharacterSheet character = new CharacterSheet();
        Map<CharacterClass, ClassBlock> classMap = new HashMap<>();
        classMap.put(CharacterClass.BARD, classBlock);
        classMap.put(CharacterClass.WARLOCK, classBlockTwo);

        character.setClasses(classMap);

        exception.expect(CharacterManagementException.class);
        exception.expectMessage("Unable to increase level past 20.");
        character.levelUp(CharacterClass.BARD);
    }

    @Test
    public void should_consume_temp_hp_first() {
        CharacterSheet character = new CharacterSheet();
        character.setTemporaryHp(5);
        character.setMaxHP(50);

        character.alterHp(-10);

        assertEquals(45, character.getCurrentHP());
        assertEquals(0, character.getTemporaryHp());
    }
}
