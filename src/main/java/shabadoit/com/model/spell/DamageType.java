package shabadoit.com.model.spell;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DamageType {
    NONE("N/A"),
    FIRE("Fire"),
    FORCE("Force"),
    ICE("Frost"),
    PHYSICAL("Physical"),
    NECROTIC("Necrotic"),
    RADIANT("Radiant"),
    THUNDER("Thunder");

    private String value;

    DamageType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
