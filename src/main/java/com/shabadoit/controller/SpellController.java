package com.shabadoit.controller;

import com.shabadoit.model.spell.Spell;
import com.shabadoit.repository.SpellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
public class SpellController {

    @Autowired
    private SpellRepository repository;

    @RequestMapping(value = "spells", method = RequestMethod.GET)
    public List<Spell> list() {
        return repository.findAll();
    }

    @RequestMapping(value = "spells/{id}", method = RequestMethod.GET)
    public Optional<Spell> get(@PathVariable String id){
        return repository.findById(id);
    }

    @RequestMapping("/spells/{name}")
    public List<Spell> getRecognition(@PathVariable("name") String name){
        return repository.findByName(name);
    }

    @RequestMapping(value = "spells", method = RequestMethod.POST)
    public Spell create(@RequestBody Spell spell) {
        return repository.insert(spell);
    }

    @RequestMapping(value = "spells/{id}", method = RequestMethod.PUT)
    public Spell update(@PathVariable String id, @RequestBody Spell spell) {
        return repository.save(spell);
    }

    @RequestMapping(value = "spells/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }
}
