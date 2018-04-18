package shabadoit.model.stats;

import lombok.Data;

@Data
public class SaveBlock {
    private SkillOrSave strength;
    private SkillOrSave constitution;
    private SkillOrSave dexterity;
    private SkillOrSave intelligence;
    private SkillOrSave wisdom;
    private SkillOrSave charisma;

    private SaveBlock() {
        //jackson constructor
    }
}
