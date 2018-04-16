package shabadoit.com.model.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveBlock {
    private SkillOrSave strength;
    private SkillOrSave constitution;
    private SkillOrSave dexterity;
    private SkillOrSave intelligence;
    private SkillOrSave wisdom;
    private SkillOrSave charisma;
}
