package shabadoit.com.service;

import shabadoit.com.model.character.CharacterSheet;

import java.util.List;
import java.util.Optional;

public interface CharacterService {
    CharacterSheet addCharacter(final CharacterSheet characterSheet);
    List<CharacterSheet> listAllCharacters();
    CharacterSheet getByName(String name);
    CharacterSheet updateById(String id, CharacterSheet characterSheet);
    void deleteById(String id);
    Optional<CharacterSheet> getById(String id);
}
