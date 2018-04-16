package shabadoit.com.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import shabadoit.com.exceptions.SpellManagementException;
import shabadoit.com.model.character.CharacterClass;
import shabadoit.com.model.spell.Spell;
import shabadoit.com.model.spell.SpellLevel;
import shabadoit.com.repository.SpellRepository;
import shabadoit.com.service.SpellService;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class SpellServiceImpl implements SpellService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpellServiceImpl.class);

    @Autowired
    private SpellRepository spellRepository;

    public Spell addSpell(Spell spell) {
        if (getByName(spell.getName()) != null) {
            throw new SpellManagementException("Spell with name " + spell.getName() + " already exists");
        }
        if (spell.getId() != null && getById(spell.getId()) != null) {
            throw new SpellManagementException("Spell with Id " + spell.getId() + " already exists");
        }
        return spellRepository.insert(spell);
    }

    @Override
    public List<Spell> getAllSpells() {
        return spellRepository.findAll();
    }

    @Override
    public Spell getByName(String name) {
        return spellRepository.findByName(name);
        //Could wrap this and log failures
    }

    @Override
    public Spell updateById(String id, Spell spell) {
        if (getById(id) != null) {
            if (id.equals(spell.getId())) {
                return spellRepository.save(spell);
            } else {
                throw new SpellManagementException("Supplied spell Ids do not match, unable to update");
            }
        } else {
            throw new SpellManagementException("Spell with Id " + id + " does not exist");
        }
    }

    @Override
    public void deleteById(String id) {
        if (getById(id) != null) {
            spellRepository.deleteById(id);
        } else {
            throw new SpellManagementException("Spell with Id " + id + " does not exist");
        }
    }

    @Override
    public Spell getById(String id) {
        Optional<Spell> spell = spellRepository.findById(id);
        if (spell.isPresent()) {
            return spell.get();
        } else {
            LOGGER.info("Id " + id + " not found, no item returned");
            return null;
        }
    }

    @Override
    public List<Spell> listByClass(CharacterClass characterClass) {
        //TODO Add error handling
        return spellRepository.findBySpellClasses(characterClass);
    }

    @Override
    public List<Spell> listBySpellLevel(SpellLevel spellLevel) {
        //TODO Add error handling
        return spellRepository.findBySpellLevel(spellLevel);
    }

    @Override
    public List<Spell> listByClassAndLevel(CharacterClass characterClass, SpellLevel spellLevel) {
        //TODO Add error handling
        return spellRepository.findBySpellClassesAndSpellLevel(characterClass, spellLevel);
    }
}
