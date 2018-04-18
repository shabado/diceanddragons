package shabadoit.com.model.stats;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

@Data
public class Stat {
    private StatName statName;
    @Positive(message = "{validation.integer.positive}") @Max(value = 20, message = "{validation.integer.statMax}")
    private int value;

    private Stat() {
        //jacksonconstructor
    }
}
