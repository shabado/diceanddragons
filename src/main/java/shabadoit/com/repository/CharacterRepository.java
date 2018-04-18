package shabadoit.com.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import shabadoit.com.model.character.CharacterSheet;

public interface CharacterRepository extends MongoRepository<CharacterSheet, String> {
    CharacterSheet getByName(String name);
    void deleteById(String id);
}
