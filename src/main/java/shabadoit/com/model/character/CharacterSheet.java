package shabadoit.com.model.character;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import shabadoit.com.model.stats.SaveBlock;
import shabadoit.com.model.stats.SkillBlock;
import shabadoit.com.model.stats.StatBlock;

import java.util.*;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "charactersheets")
public class CharacterSheet {
    @Id
    private String id;
    private String race;
    private String name;
    private int currentHP;
    private int maxHP;
    private int temporaryHp;
    @Setter(AccessLevel.NONE)
    private int characterLevel;
    private List<ClassBlock> classes;
    private StatBlock stats;
    private SaveBlock saves;
    private SkillBlock skills;
    @Setter(AccessLevel.NONE)
    private Map<HitDie, Integer> totalHitDice;

    public CharacterSheet() { }

    public void setMaxHP(int hp) {
        //ToDo Alter this to roll the Hp when I've added some kind of rolling feature
        //If you're unconscious this will cause a 'bug' where increasing max hp when unconscious
        //updates current hp to max. Can't think why that would happen, but could add a flag to check.
        if (currentHP == 0 || currentHP == maxHP) {
            currentHP = hp;
            maxHP = hp;
        } else {
            maxHP = hp;
        }
    }

    public void setCharacterLevel() {
        int i = 0;
        for (ClassBlock charClass : classes) {
            i += charClass.getClassLevel();
        }
        characterLevel = i;
    }

    public void setTotalHitDice() {
        if (totalHitDice == null)
        {
            totalHitDice = new HashMap<>();
        }

        for (ClassBlock classBlock : classes) {
            totalHitDice.put(classBlock.getClassName().getHitDie(), classBlock.getClassLevel());
        }
    }

    public void alterHp(int change) {
        int alteredHp = currentHP + change;
        if (alteredHp > maxHP) {
            currentHP = maxHP;
        } else if (alteredHp < 0) {
            currentHP = 0;
        } else {
            currentHP = alteredHp;
        }
    }

    public void levelUp(CharacterClass leveledClass) {
        if (getCharacterLevel() == 20) {
            throw new IllegalStateException("Unable to increase level past 20.");
        }

        if (classes == null) {
            classes = new ArrayList<>();
        }

        Optional<ClassBlock> classBlock = classes.stream().findFirst().filter(x -> x.getClassName() == leveledClass);

        if (classBlock.isPresent()) {
            classBlock.get().levelUp();
        } else {
            ClassBlock block = new ClassBlock(1, leveledClass);
            classes.add(block);
        }
        setCharacterLevel();
        setTotalHitDice();
    }

    //ToDo implement a way of rolling a save/hit/abilitycheck when rolling is
}
