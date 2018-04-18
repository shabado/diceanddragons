package shabadoit.model.spell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import shabadoit.model.character.CharacterClass;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "spells")
public class Spell {
    @Id
    private String id;
    @NonNull
    private SpellLevel spellLevel;
    @Indexed
    @NonNull
    private String name;
    private String description;
    private DamageType damageType;
    private List<CharacterClass> spellClasses;
    private SpellDamage damage;
    private String castingTime;
    private String components;
    private String duration;
    private String range;
    private boolean concentration;

    //Used by Spring/MongoDb
    private Spell() {}
}

