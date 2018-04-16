package shabadoit.com.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shabadoit.com.exceptions.CharacterManagementException;
import shabadoit.com.model.character.CharacterSheet;
import shabadoit.com.repository.CharacterRepository;
import shabadoit.com.service.CharacterService;

import java.util.List;
import java.util.Optional;

@Service
public class CharacterServiceImpl implements CharacterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterServiceImpl.class);

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
    public List<CharacterSheet> getAllCharacters() {
        return characterRepository.findAll();
    }

    @Override
    public CharacterSheet getByName(String name) {
        return characterRepository.findByName(name);
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
            throw new CharacterManagementException("Character with Id " + id + " does not exist");
        }
    }

    @Override
    public void deleteById(String id) {
        if (getById(id) != null) {
            characterRepository.deleteById(id);
        } else {
            throw new CharacterManagementException("Character with Id " + id + " does not exist");
        }
    }

    @Override
    public CharacterSheet getById(String id) {
        Optional<CharacterSheet> character = characterRepository.findById(id);
        if (character.isPresent()) {
            return character.get();
        } else {
            LOGGER.info("Id " + id + " not found, no item returned");
            return null;
        }
    }
}
