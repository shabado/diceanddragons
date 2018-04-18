package shabadoit.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shabadoit.com.exceptions.ResourceNotFoundException;
import shabadoit.com.exceptions.SpellManagementException;
import shabadoit.com.model.filter.SpellFilter;
import shabadoit.com.model.spell.Spell;
import shabadoit.com.service.SpellService;
import shabadoit.com.service.impl.SpellFilterService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/spells")
public class SpellController {

    private SpellService spellService;

    private SpellFilterService spellFilterService;

    @Autowired
    public SpellController(final SpellService spellService, final SpellFilterService spellFilterService) {
        this.spellService = spellService;
        this.spellFilterService = spellFilterService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Spell>> list() {
        return ResponseEntity.ok(spellService.listAllSpells());
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Spell> getById(@PathVariable String id) {
        Optional<Spell> spell = spellService.getById(id);

        if(spell.isPresent()){
            return ResponseEntity.ok(spell.get());
        } else {
            throw new ResourceNotFoundException("Spell with Id " + id + " not found.");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Spell> create(@Valid @RequestBody Spell spell) {
        return ResponseEntity.ok(spellService.addSpell(spell));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Spell> update(@Valid @PathVariable String id, @RequestBody Spell spell) {
        return ResponseEntity.ok(spellService.updateById(id, spell));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable String id) {
        spellService.deleteById(id);
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET)
    public ResponseEntity<List<Spell>> filterSpells(SpellFilter spellFilter) {
        return ResponseEntity.ok(spellFilterService.filterSpells(spellFilter));
    }
}
