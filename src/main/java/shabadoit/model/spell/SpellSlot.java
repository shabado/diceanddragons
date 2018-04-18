package shabadoit.model.spell;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
public class SpellSlot {
    @PositiveOrZero(message = "{validation.integer.notNegative}")
    @Setter(AccessLevel.NONE)
    int current;
    @PositiveOrZero(message = "{validation.integer.notNegative}")
    int max;

    public void setCurrent(int value) {
        current = (value > max) ? max : value;
    }

    private SpellSlot() {
        //jackson constructor
    }
}
