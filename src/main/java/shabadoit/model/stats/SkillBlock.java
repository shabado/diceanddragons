package shabadoit.model.stats;

import lombok.Data;

@Data
public class SkillBlock {
    private SkillOrSave acrobatics = new SkillOrSave(StatName.DEXTERITY, false);
    private SkillOrSave animalHandling = new SkillOrSave(StatName.WISDOM, false);
    private SkillOrSave arcana = new SkillOrSave(StatName.INTELLIGENCE, false);
    private SkillOrSave athletics = new SkillOrSave(StatName.STRENGTH, false);
    private SkillOrSave deception = new SkillOrSave(StatName.CHARISMA, false);
    private SkillOrSave history = new SkillOrSave(StatName.INTELLIGENCE, false);
    private SkillOrSave insight = new SkillOrSave(StatName.WISDOM, false);
    private SkillOrSave intimidation = new SkillOrSave(StatName.CHARISMA, false);
    private SkillOrSave investigation = new SkillOrSave(StatName.INTELLIGENCE, false);
    private SkillOrSave medicine = new SkillOrSave(StatName.WISDOM, false);
    private SkillOrSave nature = new SkillOrSave(StatName.INTELLIGENCE, false);
    private SkillOrSave perception = new SkillOrSave(StatName.WISDOM, false);
    private SkillOrSave performance = new SkillOrSave(StatName.CHARISMA, false);
    private SkillOrSave persuasion = new SkillOrSave(StatName.CHARISMA, false);
    private SkillOrSave religion = new SkillOrSave(StatName.INTELLIGENCE, false);
    private SkillOrSave sleightOfHand = new SkillOrSave(StatName.DEXTERITY, false);
    private SkillOrSave stealth = new SkillOrSave(StatName.DEXTERITY, false);
    private SkillOrSave survival = new SkillOrSave(StatName.WISDOM, false);

    public SkillBlock() {
        //jackson constructor
    }

    //If the object is created with missing attributes, this will ensure the stats are properly set
    public void setStats() {
        acrobatics.setStat(StatName.DEXTERITY);
        animalHandling.setStat(StatName.WISDOM);
        arcana.setStat(StatName.INTELLIGENCE);
        athletics.setStat(StatName.STRENGTH);
        deception.setStat(StatName.CHARISMA);
        history.setStat(StatName.INTELLIGENCE);
        insight.setStat(StatName.WISDOM);
        intimidation.setStat(StatName.CHARISMA);
        investigation.setStat(StatName.INTELLIGENCE);
        medicine.setStat(StatName.WISDOM);
        nature.setStat(StatName.INTELLIGENCE);
        perception.setStat(StatName.WISDOM);
        performance.setStat(StatName.CHARISMA);
        persuasion.setStat(StatName.CHARISMA);
        religion.setStat(StatName.INTELLIGENCE);
        sleightOfHand.setStat(StatName.DEXTERITY);
        stealth.setStat(StatName.DEXTERITY);
        survival.setStat(StatName.WISDOM);
    }
}
