package shabadoit.com.model.character;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import shabadoit.com.model.spell.Spell;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ClassBlock {
    @NonNull
    private int classLevel;
    @NonNull
    private CharacterClass className;
    private List<Spell> spells;

    public void levelUp() {
        classLevel+=1;
    }
}
