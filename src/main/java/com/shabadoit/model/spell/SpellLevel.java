package com.shabadoit.model.spell;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SpellLevel {
    CANTRIP("Cantrip"),
    LEVEL1("Level 1"),
    LEVEL2("Level 2"),
    LEVEL3("Level 3"),
    LEVEL4("Level 4"),
    LEVEL5("Level 5"),
    LEVEL6("Level 6");

    private String value;

    SpellLevel(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
