package shabadoit.model.character;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import shabadoit.exceptions.SpellManagementException;
import shabadoit.model.spell.Spell;
import shabadoit.model.spell.SpellLevel;
import shabadoit.model.spell.SpellSlot;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class ClassBlock {
    @NonNull
    @NotNull(message = "{validation.field.required]")
    @Positive(message = "{validation.integer.positive}")
    @Max(value = 20, message = "{validation.integer.statMax}")
    private int classLevel;
    @Valid
    private Map<SpellLevel, SpellSlot> spellSlots;
    private List<Spell> spells;

    public void levelUp() {
        classLevel += 1;
    }

    public void alterSpellSlots(SpellLevel spellLevel, int change) {
        if (spellLevel.getValue().equals(SpellLevel.CANTRIP)) {
            throw new SpellManagementException("Cantrips do not use spell slots");
        }
        if (spellSlots.containsKey(spellLevel)) {
            SpellSlot slot = spellSlots.get(spellLevel);
            int temp = slot.getMax() + change;
            if (temp <= 0) {
                spellSlots.remove(spellLevel);
            } else {
                slot.setMax(temp);
                slot.setCurrent(slot.getCurrent()+change);
            }
        }
    }

    private ClassBlock() {
        //jackson constructor
    }
}
