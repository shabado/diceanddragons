package shabadoit.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shabadoit.exceptions.CharacterManagementException;
import shabadoit.exceptions.ResourceNotFoundException;
import shabadoit.model.character.CharacterClass;
import shabadoit.model.character.CharacterSheet;
import shabadoit.repository.CharacterRepository;
import shabadoit.service.CharacterService;

import java.util.List;
import java.util.Optional;

@Service
public class CharacterServiceImpl implements CharacterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterServiceImpl.class);

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    public CharacterServiceImpl(final CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @Override
    public CharacterSheet addCharacter(CharacterSheet characterSheet) {
        if (characterSheet.getId() != null && getById(characterSheet.getId()) != null) {
            throw new CharacterManagementException("CharacterSheet with id " + characterSheet.getId() + "already exist");
        }
        return characterRepository.insert(characterSheet);
    }

    @Override
    public List<CharacterSheet> listAllCharacters() {
        return characterRepository.findAll();
    }

    @Override
    public CharacterSheet getByName(String name) {
        return characterRepository.getByName(name);
    }

    @Override
    public CharacterSheet updateById(String id, CharacterSheet characterSheet) {
        if (getById(id) != null) {
            if (id.equals(characterSheet.getId())) {
                return characterRepository.save(characterSheet);
            } else {
                throw new CharacterManagementException("Supplied character Ids do not match, unable to update");
            }
        } else {
            throw new ResourceNotFoundException("Character with Id \" + id + \" not found.\"");
        }
    }

    @Override
    public void deleteById(String id) {
        if (getById(id) != null) {
            characterRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Character with Id " + id + " not found.");
        }
    }

    @Override
    public Optional<CharacterSheet> getById(String id) {
        Optional<CharacterSheet> character = characterRepository.findById(id);
        if (!character.isPresent()) {
            LOGGER.info("Id " + id + " not found, no item returned");
        }
        return character;
    }

    @Override
    public CharacterSheet levelUp(String id, CharacterClass charClass) {
        Optional<CharacterSheet> character = getById(id);
        if (!character.isPresent()) {
            throw new ResourceNotFoundException("Character with Id \" + id + \" not found.\"");
        }
        character.get().levelUp(charClass);
        return characterRepository.save(character.get());
    }
}
