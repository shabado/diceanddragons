package shabadoit.com.model.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatBlock {
    private Stat strength;
    private Stat constitution;
    private Stat dexterity;
    private Stat intelligence;
    private Stat wisdom;
    private Stat charisma;
}
