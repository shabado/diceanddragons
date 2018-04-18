package shabadoit.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import shabadoit.model.character.CharacterSheet;

public class CharacterSheetValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CharacterSheet.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CharacterSheet charSheet = (CharacterSheet) target;
        if (validateMaxLevel(charSheet.getCharacterLevel())) {
            errors.rejectValue("classes", "validation.integer.statMax");
        }
    }

    private boolean validateMaxLevel(int charLevel) {
        return charLevel >= 20;
    }
}
