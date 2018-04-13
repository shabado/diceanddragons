package shabadoit.com;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import shabadoit.com.model.spell.Spell;
import shabadoit.com.model.spell.SpellLevel;
import shabadoit.com.repository.SpellRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class SpellsRestIT {

    @Autowired
    private SpellRepository spellRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private List<Spell> spells;
    private final String firstId = "Id";
    private final String secondId = "Id2";
    private final String firstName = "name1";
    private final String secondName = "name2";
    private final Spell firstSpell;
    private final Spell secondSpell;

    public SpellsRestIT() {
        firstSpell = new Spell(firstId, SpellLevel.LEVEL1, firstName);
        secondSpell = new Spell(secondId, SpellLevel.LEVEL1, secondName);
    }

    @Before
    public void setUp() {
        Spell firstSpell = new Spell(firstId, SpellLevel.LEVEL1, firstName);
        Spell secondSpell = new Spell(secondId, SpellLevel.LEVEL1, secondName);
        spells = new ArrayList<>(Arrays.asList(firstSpell, secondSpell));
        spellRepository.insert(spells);
    }

    @Test
    public void duplicate_name_returns_400() {
        ResponseEntity response = testRestTemplate.postForEntity("/api/v1/spells", firstSpell, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
