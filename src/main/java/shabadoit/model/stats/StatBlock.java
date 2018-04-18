package shabadoit.model.stats;

import lombok.Data;

import javax.validation.Valid;

@Data
public class StatBlock {
    @Valid
    private Stat strength;
    @Valid
    private Stat constitution;
    @Valid
    private Stat dexterity;
    @Valid
    private Stat intelligence;
    @Valid
    private Stat wisdom;
    @Valid
    private Stat charisma;

    private StatBlock() {
        //Jackson constructor
    }
}
