package shabadoit.model.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SkillOrSave {
    private StatName stat;
    private boolean proficient;

    private SkillOrSave() {
        //jacksonConstructor
    }
}
