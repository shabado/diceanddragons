package com.shabadoit.service.impl;

import com.shabadoit.exceptions.SpellManagementException;
import com.shabadoit.model.spell.Spell;
import com.shabadoit.repository.SpellRepository;
import com.shabadoit.service.SpellService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpellServiceImpl implements SpellService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpellServiceImpl.class);


    @Autowired
    private SpellRepository spellRepository;

    public void addSpell(Spell spell) {
        if (spellRepository.findByName(spell.getName()) != null) {
            throw new SpellManagementException("Spell with name " + spell.getName() + " already exists");
        }
        spellRepository.insert(spell);
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
    public void updateById(String id, Spell spell) {
        Optional<Spell> existingSpell = spellRepository.findById(id);
        if (existingSpell.isPresent()) {
            if (id.equals(spell.getId())) {
                spellRepository.save(spell);
            } else {
                throw new SpellManagementException("Supplied spell Ids do not match, unable to update");
            }
        } else {
            throw new SpellManagementException("Spell with Id " + id + " does not exist");
        }
    }

    @Override
    public void deleteById(String id) {
        Optional<Spell> spell = spellRepository.findById(id);
        if (spell.isPresent()) {
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
}
