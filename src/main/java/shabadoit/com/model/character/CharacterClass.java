package shabadoit.com.model.character;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CharacterClass {
    BARD("Bard"),
    CLERIC("Cleric"),
    DRUID("Druid"),
    PALADIN("Paladin"),
    RANGER("Ranger"),
    SORCERER("Sorcerer"),
    WARLOCK("Warlock"),
    WIZARD("Wizard");

    private String value;

    CharacterClass(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}