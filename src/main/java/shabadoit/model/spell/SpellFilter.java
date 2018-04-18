package shabadoit.model.spell;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import shabadoit.model.character.CharacterClass;

@Data
@AllArgsConstructor
@Builder
public class SpellFilter {
    private String name;
    private CharacterClass characterClass;
    private SpellLevel spellLevel;

    private SpellFilter(){}
}
