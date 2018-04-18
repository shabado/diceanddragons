package shabadoit.com.model.character;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import shabadoit.com.model.spell.Spell;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ClassBlock {
    @NonNull
    @NotNull(message = "{validation.field.required]")
    @Positive(message = "{validation.integer.positive}") @Max(value = 20, message = "{validation.integer.statMax}")
    private int classLevel;
    private List<Spell> spells;

    public void levelUp() {
        classLevel += 1;
    }

    private ClassBlock() {
        //jackson constructor
    }
}
