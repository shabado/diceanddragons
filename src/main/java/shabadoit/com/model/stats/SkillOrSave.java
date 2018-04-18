package shabadoit.com.model.stats;

import lombok.Data;

@Data
public class SkillOrSave {
    private StatName stat;
    private boolean proficient;

    private SkillOrSave() {
        //jacksonConstructor
    }
}
