package shabadoit.com.model.character;

import com.fasterxml.jackson.annotation.JsonValue;

public enum HitDie {
    DSIX("d6"),
    DEIGHT("d8"),
    DTEN("d10"),
    DTWELVE("d12");

    private String value;

    HitDie(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
