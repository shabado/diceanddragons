package shabadoit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import shabadoit.model.character.CharacterSheet;

public interface CharacterRepository extends MongoRepository<CharacterSheet, String> {
    CharacterSheet getByName(String name);
    void deleteById(String id);
}
