package shabadoit.service;

import shabadoit.model.character.CharacterClass;
import shabadoit.model.character.CharacterSheet;

import java.util.List;
import java.util.Optional;

public interface CharacterService {
    CharacterSheet addCharacter(final CharacterSheet characterSheet);
    List<CharacterSheet> listAllCharacters();
    CharacterSheet getByName(String name);
    CharacterSheet updateById(String id, CharacterSheet characterSheet);
    void deleteById(String id);
    Optional<CharacterSheet> getById(String id);
    CharacterSheet levelUp(String id, CharacterClass charClass);
}
