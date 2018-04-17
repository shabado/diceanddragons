package shabadoit.com.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import shabadoit.com.model.character.CharacterClass;
import shabadoit.com.model.character.CharacterSheet;
import shabadoit.com.model.character.ClassBlock;
import shabadoit.com.model.character.HitDie;

import java.util.Arrays;
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
        ClassBlock classBlock = new ClassBlock(1, CharacterClass.BARD);
        CharacterSheet character = new CharacterSheet();
        character.setClasses(Arrays.asList(classBlock));

        character.levelUp(CharacterClass.BARD);

        assertEquals(2, character.getCharacterLevel());
    }

    @Test
    public void should_add_new_class_when_level_up() {
        CharacterSheet character = new CharacterSheet();
        character.levelUp(CharacterClass.BARD);

        assertEquals(1, character.getCharacterLevel());
        assertTrue(character.getClasses().stream().anyMatch(x -> x.getClassName() == CharacterClass.BARD));
    }

    @Test
    public void should_set_hit_die() {
        ClassBlock classBlock = new ClassBlock(3, CharacterClass.BARD);
        CharacterSheet character = new CharacterSheet();
        character.setClasses(Arrays.asList(classBlock));

        character.setTotalHitDice();
        Map<HitDie, Integer> hitDie = character.getTotalHitDice();

        assertEquals(3, hitDie.get(CharacterClass.BARD.getHitDie()).intValue());
    }

    @Test
    public void should_update_hit_die_on_level_up() {
        ClassBlock classBlock = new ClassBlock(3, CharacterClass.BARD);
        CharacterSheet character = new CharacterSheet();
        character.setClasses(Arrays.asList(classBlock));

        character.setTotalHitDice();

        character.levelUp(CharacterClass.BARD);

        Map<HitDie, Integer> hitDie = character.getTotalHitDice();

        assertEquals(4, hitDie.get(CharacterClass.BARD.getHitDie()).intValue());
    }

    @Test
    public void should_set_character_level() {
        ClassBlock classBlock = new ClassBlock(3, CharacterClass.BARD);
        CharacterSheet character = new CharacterSheet();
        character.setClasses(Arrays.asList(classBlock));

        character.setCharacterLevel();

        assertEquals(3, character.getCharacterLevel());
    }

    @Test
    public void should_set_character_level_multiple_classes() {
        ClassBlock classBlock = new ClassBlock(3, CharacterClass.BARD);
        ClassBlock classBlockTwo = new ClassBlock(12, CharacterClass.WIZARD);
        CharacterSheet character = new CharacterSheet();
        character.setClasses(Arrays.asList(classBlock, classBlockTwo));

        character.setCharacterLevel();

        assertEquals(15, character.getCharacterLevel());
    }

    @Test
    public void should_throw_if_levelling_past_20() {
        ClassBlock classBlock = new ClassBlock(8, CharacterClass.BARD);
        ClassBlock classBlockTwo = new ClassBlock(12, CharacterClass.WIZARD);
        CharacterSheet character = new CharacterSheet();
        character.setClasses(Arrays.asList(classBlock, classBlockTwo));

        character.levelUp(CharacterClass.BARD);

        exception.expect(IllegalStateException.class);
        exception.expectMessage("Unable to increase level past 20.");
    }
}
