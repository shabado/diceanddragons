package shabadoit.com.model.character;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import shabadoit.com.model.stats.SaveBlock;
import shabadoit.com.model.stats.SkillBlock;
import shabadoit.com.model.stats.StatBlock;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "charactersheets")
public class CharacterSheet {
    @Id
    private String id;
    private String name;
    @JsonProperty("class")
    private CharacterClass className;
    private int currentHP;
    private int maxHP;
    private int characterLevel;
    private StatBlock stats;
    private SaveBlock saves;
    private SkillBlock skills;

    //ToDO implement something for Hit die (inc rolling to add hp)
    //ToDo implment a way of rolling a save/hit/abilitycheck
    //ToDo implement a way of modifying current Hp
    //ToDo long term, implement something for multiclassing
}
