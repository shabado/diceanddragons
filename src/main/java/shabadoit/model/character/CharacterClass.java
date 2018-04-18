package shabadoit.model.character;

import com.fasterxml.jackson.annotation.JsonValue;
import shabadoit.model.stats.StatName;

public enum CharacterClass {
    BARBARIAN("Barbarian", HitDie.DTWELVE, null),
    BARD("Bard", HitDie.DEIGHT, StatName.CHARISMA),
    CLERIC("Cleric", HitDie.DEIGHT, StatName.WISDOM),
    DRUID("Druid", HitDie.DEIGHT, StatName.WISDOM),
    FIGHTER("Figher", HitDie.DTEN, null),
    PALADIN("Paladin", HitDie.DTEN, StatName.CHARISMA),
    RANGER("Ranger", HitDie.DTEN, StatName.WISDOM),
    ROGUE("Rogue", HitDie.DEIGHT, null),
    SORCERER("Sorcerer", HitDie.DSIX, StatName.CHARISMA),
    WARLOCK("Warlock", HitDie.DEIGHT, StatName.CHARISMA),
    WIZARD("Wizard", HitDie.DSIX, StatName.INTELLIGENCE);

    private String value;
    private HitDie hitDie;
    private StatName castingStat;

    CharacterClass(String value, HitDie hitDie, StatName castingStat) {
        this.value = value;
        this.hitDie = hitDie;
        this.castingStat = castingStat;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }

    public HitDie getHitDie() {
        return this.hitDie;
    }
}