package shabadoit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import shabadoit.exceptions.ResourceNotFoundException;
import shabadoit.model.character.CharacterClass;
import shabadoit.model.character.CharacterSheet;
import shabadoit.service.CharacterService;
import shabadoit.validator.CharacterSheetValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/characters")
public class CharacterController {

    private CharacterService characterService;

    @InitBinder("characterSheet")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new CharacterSheetValidator());
    }

    @Autowired
    public CharacterController(final CharacterService characterService) {
        this.characterService = characterService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CharacterSheet>> list() {
        return ResponseEntity.ok(characterService.listAllCharacters());
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<CharacterSheet> getById(@PathVariable String id) {
        Optional<CharacterSheet> character = characterService.getById(id);

        if (character.isPresent()) {
            return ResponseEntity.ok(character.get());
        } else {
            throw new ResourceNotFoundException("Character with Id " + id + " not found.");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CharacterSheet> create(@Valid @RequestBody CharacterSheet character) {
        return ResponseEntity.ok(characterService.addCharacter(character));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<CharacterSheet> update(@Valid @PathVariable String id,
                                                 @RequestBody CharacterSheet character) {
        return ResponseEntity.ok(characterService.updateById(id, character));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable String id) {
        characterService.deleteById(id);
    }

    @RequestMapping(value = "/{id}/levelup/{charClass}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public CharacterSheet levelUp(@PathVariable String id,
                                  @PathVariable CharacterClass charClass) {
        return characterService.levelUp(id, charClass);
    }

    @RequestMapping(value = "/{id}/alterHp", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public CharacterSheet alterHp(@PathVariable String id, @RequestBody int change) {
        return characterService.alterHp(id, change);
    }

    @RequestMapping(value = "/{id}/setTempHp", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public CharacterSheet setTempUp(@PathVariable String id, @RequestBody int tempHp) {
        return characterService.setTempHp(id, tempHp);
    }
}
