package shabadoit.com;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;
import shabadoit.com.model.spell.Spell;
import shabadoit.com.model.spell.SpellLevel;
import shabadoit.com.repository.SpellRepository;

import java.net.URI;
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

    @After
    public void tearDown() {
        spellRepository.deleteAll();
    }

    @Test
    public void should_add_new_spell() {
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
    public void should_return_all_spells() {
        ResponseEntity<Spell[]> response = testRestTemplate.getForEntity("/api/v1/spells", Spell[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(spells, containsInAnyOrder(response.getBody()));
    }

    @Test
    public void duplicate_insert_id_returns_400() {
        Spell duplicateId = new Spell(SpellLevel.LEVEL1, "DuplicateId");
        duplicateId.setId(firstSpell.getId());

        ResponseEntity response = testRestTemplate.postForEntity("/api/v1/spells", duplicateId, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void should_get_by_id() {
        ResponseEntity<Spell> response =
                testRestTemplate.getForEntity("/api/v1/spells/" + firstSpell.getId(), Spell.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(firstSpell, response.getBody());
    }

    @Test
    public void should_update_spell() {
        Spell updatedSpell = new Spell(SpellLevel.LEVEL6, "New Name");
        updatedSpell.setId(firstSpell.getId());

        URI uri = UriComponentsBuilder.fromUriString(testRestTemplate.getRootUri() + "/api/v1/spells/")
                .pathSegment(firstSpell.getId()).build().toUri();

        RequestEntity<Spell> requestEntity = new RequestEntity<>(updatedSpell, HttpMethod.PUT, uri);

        ResponseEntity<Spell> response =
                testRestTemplate.exchange(requestEntity, Spell.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedSpell, response.getBody());

        //Check spell has updated
        ResponseEntity<Spell> getResponse =
                testRestTemplate.getForEntity("/api/v1/spells/" + firstSpell.getId(), Spell.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(updatedSpell, getResponse.getBody());
    }

    @Test
    public void should_bad_request_if_update_Id_mismatch() {
        Spell updatedSpell = new Spell(SpellLevel.LEVEL6, "New Name");
        updatedSpell.setId("MismatchId");

        URI uri = UriComponentsBuilder.fromUriString(testRestTemplate.getRootUri() + "/api/v1/spells/")
                .pathSegment(firstSpell.getId()).build().toUri();

        RequestEntity<Spell> requestEntity = new RequestEntity<>(updatedSpell, HttpMethod.PUT, uri);

        ResponseEntity<Spell> response =
                testRestTemplate.exchange(requestEntity, Spell.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        //Check spell has not updated
        ResponseEntity<Spell> getResponse =
                testRestTemplate.getForEntity("/api/v1/spells/" + firstSpell.getId(), Spell.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(firstSpell, getResponse.getBody());
    }

    @Test
    public void should_delete_spell() {
        URI uri = UriComponentsBuilder.fromUriString(testRestTemplate.getRootUri() + "/api/v1/spells/")
                .pathSegment(firstSpell.getId()).build().toUri();

        RequestEntity<Spell> requestEntity = new RequestEntity<>(HttpMethod.DELETE, uri);

        ResponseEntity response =
                testRestTemplate.exchange(requestEntity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        //Check Spell deleted
        ResponseEntity<Spell> getResponse =
                testRestTemplate.getForEntity("/api/v1/spells/" + firstSpell.getId(), Spell.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(firstSpell, getResponse.getBody());
    }

    //Delete
    //Spells by class
    //Spells by level
    //Spells by class & level
    //All filters
}
