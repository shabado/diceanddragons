package shabadoit.com.service;

import shabadoit.com.model.character.CharacterSheet;

import java.util.List;

public interface CharacterService {
    CharacterSheet addCharacter(final CharacterSheet characterSheet);
    List<CharacterSheet> getAllCharacters();
    CharacterSheet getByName(String name);
    CharacterSheet updateById(String id, CharacterSheet characterSheet);
    void deleteById(String id);
    CharacterSheet getById(String id);
}
