package shabadoit.com.model.stats;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatName {
    STRENGTH("d4"),
    CONSTITUTION("d6"),
    DEXTERITY("d8"),
    INTELLIGENCE("d10"),
    WISDOM("d12"),
    CHARISMA("d20");

    private String value;

    StatName(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
