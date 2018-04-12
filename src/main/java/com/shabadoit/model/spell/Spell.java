package com.shabadoit.model.spell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shabadoit.model.character.CharacterClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "spells")
public class Spell {
    @Id
    private String id;
    private SpellLevel spellLevel;
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
}

