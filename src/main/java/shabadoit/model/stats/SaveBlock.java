package shabadoit.model.stats;

import lombok.Data;

@Data
public class SaveBlock {
    private SkillOrSave strength = new SkillOrSave(StatName.STRENGTH, false);
    private SkillOrSave constitution = new SkillOrSave(StatName.CONSTITUTION, false);
    private SkillOrSave dexterity = new SkillOrSave(StatName.DEXTERITY, false);
    private SkillOrSave intelligence = new SkillOrSave(StatName.INTELLIGENCE, false);
    private SkillOrSave wisdom = new SkillOrSave(StatName.WISDOM, false);
    private SkillOrSave charisma = new SkillOrSave(StatName.CHARISMA, false);

    public SaveBlock() {
        //jackson constructor
    }
}
