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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class SpellsRestIT {

    @Autowired
    private SpellRepository spellRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private List<Spell> spells;
    private final String firstName = "name1";
    private final String secondName = "name2";
    private Spell firstSpell;
    private Spell secondSpell;


    @Before
    public void setUp() {
        spells = new ArrayList<>(Arrays.asList(new Spell(SpellLevel.LEVEL1, firstName), new Spell(SpellLevel.LEVEL1, secondName)));
        List<Spell> insertedSpells = spellRepository.insert(spells);
        firstSpell = insertedSpells.get(0);
        secondSpell = insertedSpells.get(1);
    }

    @Test
    public void can_add_new_spell() {
        Spell newSpell = new Spell(SpellLevel.LEVEL1, "New spell");
        ResponseEntity<Spell> response = testRestTemplate.postForEntity("/api/v1/spells", newSpell, Spell.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    public void duplicate_name_returns_400() {
        ResponseEntity response = testRestTemplate.postForEntity("/api/v1/spells", firstSpell, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void returns_all_spells() {
        ResponseEntity<Spell[]> response = testRestTemplate.getForEntity("/api/v1/spells", Spell[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(spells, containsInAnyOrder(response.getBody()));
    }

    //Duplicate Id throws 400
    //Get by Id
    //Get by name
    //Update
    //Delete
    //Failed update if params dont match
}
