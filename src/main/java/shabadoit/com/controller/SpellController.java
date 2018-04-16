package shabadoit.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shabadoit.com.exceptions.SpellManagementException;
import shabadoit.com.model.filter.SpellFilter;
import shabadoit.com.model.spell.Spell;
import shabadoit.com.service.impl.SpellFilterService;
import shabadoit.com.service.SpellService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/spells")
public class SpellController {

    private SpellService spellService;

    private SpellFilterService spellFilterService;

    @Autowired
    public SpellController(final SpellService spellService, final SpellFilterService spellFilterService){
        this.spellService = spellService;
        this.spellFilterService = spellFilterService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Spell> list() {
        return spellService.listAllSpells();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Spell getById(@PathVariable String id) {
        return spellService.getById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Spell create(@RequestBody Spell spell) {
        return spellService.addSpell(spell);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Spell update(@PathVariable String id, @RequestBody Spell spell) {
        return spellService.updateById(id, spell);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id) {
        spellService.deleteById(id);
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET)
    public List<Spell> filterSpells(SpellFilter spellFilter) {
        return spellFilterService.filterSpells(spellFilter);
    }

    @ExceptionHandler(SpellManagementException.class)
    void handleBadRequest(SpellManagementException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}
