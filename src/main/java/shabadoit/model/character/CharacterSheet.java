package shabadoit.model.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import shabadoit.exceptions.CharacterManagementException;
import shabadoit.model.stats.SaveBlock;
import shabadoit.model.stats.SkillBlock;
import shabadoit.model.stats.StatBlock;
import shabadoit.serializer.CharacterSheetDeserializer;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@JsonDeserialize(using = CharacterSheetDeserializer.class)
@Document(collection = "charactersheets")
public class CharacterSheet {
    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterSheet.class);

    @Id
    private String id;
    private String race;
    @NotNull(message = "{validation.field.required}")
    private String name;
    @Setter(AccessLevel.NONE)
    private int proficiencyBonus;
    @PositiveOrZero(message = "{validation.integer.notNegative}")
    private int currentHP;
    @Positive(message = "{validation.integer.positive}")
    private int maxHP;
    @PositiveOrZero(message = "{validation.integer.notNegative}")
    private int temporaryHp;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter(AccessLevel.NONE)
    @Valid
    private int characterLevel;
    @Valid
    @Setter(AccessLevel.NONE)
    @NotNull
    @JsonSetter(nulls = Nulls.SKIP)
    private Map<CharacterClass, ClassBlock> classes;
    @Valid
    @NotNull
    private StatBlock stats;
    private SaveBlock saves;
    private SkillBlock skills;
    private List<String> proficiencies;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter(AccessLevel.NONE)
    private Map<HitDie, Integer> totalHitDice;

    public CharacterSheet() {
        classes = new HashMap<>();
        updateDerivedAttributes();
    }

    private void updateDerivedAttributes() {
        setTotalHitDice();
        setCharacterLevel();
        setProficiencyBonus();
    }

    public void setMaxHP(int hp) {
        //TODO Alter this to roll the Hp when I've added some kind of rolling feature
        //If you're unconscious this will cause a 'bug' where increasing max hp when unconscious
        //updates current hp to max. Can't think why that would happen, but could add a flag to check.
        if (currentHP == 0 || currentHP == maxHP) {
            currentHP = hp;
            maxHP = hp;
        } else {
            maxHP = hp;
        }
    }

    private void setCharacterLevel() {
        int i = 0;

        if (classes.values().isEmpty()) {
            characterLevel = 0;
        }

        for (ClassBlock charClass : classes.values()) {
            i += charClass.getClassLevel();
        }
        characterLevel = i;
    }

    private void setProficiencyBonus() {
        int val = (characterLevel-1)/4;
        proficiencyBonus = val-(val%1)+2;
    }

    private void setTotalHitDice() {
        totalHitDice = new HashMap<>();
        classes.forEach((charClass, classBlock) -> {
            if (totalHitDice.containsKey(charClass.getHitDie())) {
                totalHitDice.put(charClass.getHitDie(),
                        totalHitDice.get(charClass.getHitDie())+ classBlock.getClassLevel());
            } else {
                totalHitDice.put(charClass.getHitDie(), classBlock.getClassLevel());
            }
        });
    }

    public CharacterSheet alterHp(int change) {
        if (change < 0) {
            inflictDamage(change);
        } else {
            healDamage(change);
        }
        return this;
    }

    public CharacterSheet levelUp(CharacterClass leveledClass) {
        if (characterLevel >= 20) {
            throw new CharacterManagementException("Unable to increase level past 20.");
        }

        if (classes.containsKey(leveledClass)) {
            classes.get(leveledClass).levelUp();
        } else {
            classes.put(leveledClass, new ClassBlock(1));
        }
        updateDerivedAttributes();
        return this;
    }

    private void inflictDamage(int damage) {
        if (temporaryHp > 0) {
            int temp = temporaryHp + damage;
            if (temp > 0) {
                temporaryHp = temp;
            } else {
                temporaryHp = 0;
                inflictDamage(temp);
            }
        } else {
            int alteredHp = currentHP + damage;
            currentHP = alteredHp > maxHP ? maxHP : alteredHp;
        }
        if (currentHP < 0) {
            currentHP = 0;
        }
    }

    private void healDamage(int heal) {
        int alteredHp = currentHP + heal;
        if (alteredHp > maxHP) {
            currentHP = maxHP;
        } else {
            currentHP = alteredHp;
        }
    }

    public final void setClasses(Map<CharacterClass, ClassBlock> updatedClasses) {
        int updatedLevel = 0;
        for (ClassBlock charClass : updatedClasses.values()) {
            updatedLevel += charClass.getClassLevel();
        }

        if (updatedLevel > 20) {
            throw new CharacterManagementException("Total level cannot exceed 20.");
        }

        classes = updatedClasses;
        updateDerivedAttributes();
    }

    public CharacterSheet addClass(CharacterClass characterClass, ClassBlock classBlock) {
        if (classes.containsKey(characterClass)) {
            throw new CharacterManagementException(characterClass.getValue() + " already exists");
        }
        if (getCharacterLevel() + classBlock.getClassLevel() > 20) {
            throw new CharacterManagementException("Total level cannot exceed 20.");
        }
        classes.put(characterClass, classBlock);
        updateDerivedAttributes();
        return this;
    }

    public CharacterSheet removeClass(CharacterClass characterClass) {
        if (!classes.containsKey(characterClass)) {
            throw new CharacterManagementException(characterClass.getValue() + " does not exist exists");
        }
        classes.remove(characterClass);
        updateDerivedAttributes();
        return this;
    }

    //TODO implement a way of rolling a save/hit/abilitycheck when rolling is
}
