package com.shabadoit.controller;

import com.shabadoit.model.spell.Spell;
import com.shabadoit.service.SpellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class SpellController {

    @Autowired
    private SpellService spellService;

    @RequestMapping(value = "spells", method = RequestMethod.GET)
    public List<Spell> list() {
        return spellService.getAllSpells();
    }

    @RequestMapping(value = "spells/{id}", method = RequestMethod.GET)
    public Spell get(@PathVariable String id){
        return spellService.getById(id);
    }

    @RequestMapping("/spells/{name}")
    public Spell getRecognition(@PathVariable("name") String name){
        return spellService.getByName(name);
    }

    @RequestMapping(value = "spells", method = RequestMethod.POST)
    public void create(@RequestBody Spell spell) {
        spellService.addSpell(spell);
    }

    @RequestMapping(value = "spells/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable String id, @RequestBody Spell spell) {
        spellService.updateById(id, spell);
    }

    @RequestMapping(value = "spells/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id) {
        spellService.deleteById(id);
    }
}
