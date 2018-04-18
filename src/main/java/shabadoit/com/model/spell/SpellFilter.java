package shabadoit.com.model.spell;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import shabadoit.com.model.character.CharacterClass;
import shabadoit.com.model.spell.SpellLevel;

@Data
@AllArgsConstructor
@Builder
public class SpellFilter {
    private String name;
    private CharacterClass characterClass;
    private SpellLevel spellLevel;

    private SpellFilter(){}
}
