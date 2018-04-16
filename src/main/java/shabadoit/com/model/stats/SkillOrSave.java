package shabadoit.com.model.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SkillOrSave {
    private StatName stat;
    private boolean proficient;
}
