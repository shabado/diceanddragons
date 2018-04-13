package shabadoit.com.model.spell;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DamageDice {
    DFOUR("d4"),
    DSIX("d6"),
    DEIGHT("d8"),
    DTEN("d10"),
    DTWELVE("d12"),
    DTWENTY("d20"),
    DHUNDRED("d100"),
    NONE(null);

    private String value;

    DamageDice(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
