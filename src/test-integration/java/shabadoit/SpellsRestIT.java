package shabadoit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;
import shabadoit.model.character.CharacterClass;
import shabadoit.model.spell.SpellFilter;
import shabadoit.model.spell.Spell;
import shabadoit.model.spell.SpellLevel;
import shabadoit.repository.SpellRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        Spell spellOne = new Spell(SpellLevel.LEVEL1, firstName);
        Spell spellTwo = new Spell(SpellLevel.LEVEL2, secondName);

        spells = new ArrayList<>(Arrays.asList(spellOne, spellTwo));
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

        RequestEntity requestEntity = new RequestEntity<>(updatedSpell, HttpMethod.PUT, uri);

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

        RequestEntity requestEntity = new RequestEntity<>(updatedSpell, HttpMethod.PUT, uri);

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

        RequestEntity requestEntity = new RequestEntity<>(HttpMethod.DELETE, uri);

        ResponseEntity response =
                testRestTemplate.exchange(requestEntity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        //Check Spell deleted
        ResponseEntity<Spell> getResponse =
                testRestTemplate.getForEntity("/api/v1/spells/" + firstSpell.getId(), Spell.class);

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    public void should_filter_by_class() {
        List<Spell> dbSpells = setUpForFilterTests();

        //Populate filter with first spell data to ensure it returns expected results
        CharacterClass classToFilter = dbSpells.get(0).getSpellClasses().get(0);
        SpellFilter filter = SpellFilter.builder().characterClass(classToFilter).build();

        URI uri = UriComponentsBuilder.fromUriString(testRestTemplate.getRootUri() + "/api/v1/spells/filter/")
                .queryParam("name", filter.getName())
                .queryParam("characterClass", filter.getCharacterClass())
                .queryParam("spellLevel", filter.getSpellLevel())
                .build().toUri();

        RequestEntity requestEntity = new RequestEntity<>(HttpMethod.GET, uri);
        ResponseEntity<List<Spell>> response =
                testRestTemplate.exchange(requestEntity, new ParameterizedTypeReference<List<Spell>>() {
                });

        List<Spell> expectedSpells = dbSpells.stream()
                .filter(x -> x.getSpellClasses().contains(classToFilter)).collect(Collectors.toList());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSpells, response.getBody());
    }

    @Test
    public void should_filter_by_level() {
        List<Spell> dbSpells = setUpForFilterTests();

        //Populate filter with first spell data to ensure it returns expected results
        SpellLevel spellLevelFilter = dbSpells.get(0).getSpellLevel();
        SpellFilter filter = SpellFilter.builder().spellLevel(spellLevelFilter).build();

        URI uri = UriComponentsBuilder.fromUriString(testRestTemplate.getRootUri() + "/api/v1/spells/filter/")
                .queryParam("name", filter.getName())
                .queryParam("characterClass", filter.getCharacterClass())
                .queryParam("spellLevel", filter.getSpellLevel())
                .build().toUri();

        RequestEntity requestEntity = new RequestEntity<>(HttpMethod.GET, uri);
        ResponseEntity<List<Spell>> response =
                testRestTemplate.exchange(requestEntity, new ParameterizedTypeReference<List<Spell>>() {
                });

        List<Spell> expectedSpells = dbSpells.stream()
                .filter(x -> x.getSpellLevel().equals(spellLevelFilter)).collect(Collectors.toList());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSpells, response.getBody());
    }

    @Test
    public void should_filter_by_class_and_level() {
        List<Spell> dbSpells = setUpForFilterTests();

        //Populate filter with first spell data to ensure it returns expected results
        CharacterClass classToFilter = dbSpells.get(0).getSpellClasses().get(0);
        SpellLevel spellLevelFilter = dbSpells.get(0).getSpellLevel();
        SpellFilter filter = SpellFilter.builder().characterClass(classToFilter).spellLevel(spellLevelFilter).build();

        URI uri = UriComponentsBuilder.fromUriString(testRestTemplate.getRootUri() + "/api/v1/spells/filter/")
                .queryParam("name", filter.getName())
                .queryParam("characterClass", filter.getCharacterClass())
                .queryParam("spellLevel", filter.getSpellLevel())
                .build().toUri();

        RequestEntity requestEntity = new RequestEntity<>(HttpMethod.GET, uri);
        ResponseEntity<List<Spell>> response =
                testRestTemplate.exchange(requestEntity, new ParameterizedTypeReference<List<Spell>>() {
                });

        List<Spell> expectedSpells = dbSpells.stream()
                .filter(x -> x.getSpellLevel().equals(spellLevelFilter))
                .filter(x -> x.getSpellClasses().contains(classToFilter))
                .collect(Collectors.toList());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSpells, response.getBody());
    }

    @Test
    public void should_filter_by_name() {
        SpellFilter filter = SpellFilter.builder().name(firstSpell.getName()).build();

        URI uri = UriComponentsBuilder.fromUriString(testRestTemplate.getRootUri() + "/api/v1/spells/filter/")
                .queryParam("name", filter.getName())
                .queryParam("characterClass", filter.getCharacterClass())
                .queryParam("spellLevel", filter.getSpellLevel())
                .build().toUri();

        RequestEntity requestEntity = new RequestEntity<>(HttpMethod.GET, uri);
        ResponseEntity<List<Spell>> response =
                testRestTemplate.exchange(requestEntity, new ParameterizedTypeReference<List<Spell>>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(firstSpell, response.getBody().get(0));
    }

    @Test
    public void should_filter_on_partial_name_match() {
        List<Spell> dbSpells = setUpForFilterTests();

        //Populate filter with first spell data to ensure it returns expected results
        String filterName = dbSpells.get(0).getName();
        SpellFilter filter = SpellFilter.builder().name(filterName).build();

        URI uri = UriComponentsBuilder.fromUriString(testRestTemplate.getRootUri() + "/api/v1/spells/filter/")
                .queryParam("name", filter.getName())
                .queryParam("characterClass", filter.getCharacterClass())
                .queryParam("spellLevel", filter.getSpellLevel())
                .build().toUri();

        RequestEntity requestEntity = new RequestEntity<>(HttpMethod.GET, uri);
        ResponseEntity<List<Spell>> response =
                testRestTemplate.exchange(requestEntity, new ParameterizedTypeReference<List<Spell>>() {
                });

        List<Spell> expectedSpells = dbSpells.stream()
                .filter(x -> x.getName().contains(filterName)).collect(Collectors.toList());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSpells, response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void should_filter_by_all_filters() {
        List<Spell> dbSpells = setUpForFilterTests();

        //Populate filter with first spell data to ensure it returns expected results
        CharacterClass classToFilter = dbSpells.get(0).getSpellClasses().get(0);
        SpellLevel spellLevelFilter = dbSpells.get(0).getSpellLevel();
        String filterName = dbSpells.get(0).getName();
        SpellFilter filter = SpellFilter.builder().characterClass(classToFilter)
                .name(filterName).spellLevel(spellLevelFilter).build();

        URI uri = UriComponentsBuilder.fromUriString(testRestTemplate.getRootUri() + "/api/v1/spells/filter/")
                .queryParam("name", filter.getName())
                .queryParam("characterClass", filter.getCharacterClass())
                .queryParam("spellLevel", filter.getSpellLevel())
                .build().toUri();

        RequestEntity requestEntity = new RequestEntity<>(HttpMethod.GET, uri);
        ResponseEntity<List<Spell>> response =
                testRestTemplate.exchange(requestEntity, new ParameterizedTypeReference<List<Spell>>() {
                });

        List<Spell> expectedSpells = dbSpells.stream()
                .filter(x -> x.getSpellLevel().equals(spellLevelFilter))
                .filter(x -> x.getSpellClasses().contains(classToFilter))
                .filter(x -> x.getName().contains(filterName))
                .collect(Collectors.toList());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSpells, response.getBody());
    }

    private List<Spell> setUpForFilterTests() {
        spellRepository.deleteAll();

        Spell spellOne = new Spell(SpellLevel.LEVEL1, firstName);
        spellOne.setSpellClasses(Arrays.asList(CharacterClass.BARD));
        Spell spellTwo = new Spell(SpellLevel.LEVEL2, secondName);
        spellTwo.setSpellClasses(Arrays.asList(CharacterClass.WIZARD));
        Spell spellThree = new Spell(SpellLevel.LEVEL1, firstName + "extraChar");
        spellThree.setSpellClasses(Arrays.asList(CharacterClass.BARD));
        Spell spellFour = new Spell(SpellLevel.LEVEL2, secondName + "extraChar");
        spellFour.setSpellClasses(Arrays.asList(CharacterClass.WIZARD));

        spells = new ArrayList<>(Arrays.asList(spellOne, spellTwo, spellThree, spellFour));
        return spellRepository.insert(spells);
    }
}
