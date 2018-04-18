package shabadoit.model.stats;

import lombok.Data;

@Data
public class SkillBlock {
    private SkillOrSave acrobatics;
    private SkillOrSave animalHandling;
    private SkillOrSave arcana;
    private SkillOrSave athletics;
    private SkillOrSave deception;
    private SkillOrSave history;
    private SkillOrSave insight;
    private SkillOrSave intimidation;
    private SkillOrSave investigation;
    private SkillOrSave medicine;
    private SkillOrSave nature;
    private SkillOrSave perception;
    private SkillOrSave performance;
    private SkillOrSave persuasion;
    private SkillOrSave religion;
    private SkillOrSave sleightOfHand;
    private SkillOrSave stealth;
    private SkillOrSave survival;

    private SkillBlock() {
        //jackson constructor
    }
}
