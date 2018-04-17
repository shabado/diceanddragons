package shabadoit.com.model.stats;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatName {
    STRENGTH("Strength"),
    CONSTITUTION("Constitution"),
    DEXTERITY("Dexterity"),
    INTELLIGENCE("Intelligence"),
    WISDOM("Wisdom"),
    CHARISMA("Charisma");

    private String value;

    StatName(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
