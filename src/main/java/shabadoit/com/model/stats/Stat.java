package shabadoit.com.model.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Stat {
    private StatName statName;
    private int modifier;
}
