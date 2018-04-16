package shabadoit.com.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shabadoit.com.model.spell.Spell;
import shabadoit.com.model.spell.SpellLevel;
import shabadoit.com.repository.SpellRepository;

public class SpellServiceTest {
    @InjectMocks
    private SpellService spellService;

    @Mock
    private SpellRepository spellRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_add_spell() {
        Spell spell = new Spell(SpellLevel.LEVEL1, "Name");

        spellService.addSpell(spell);
    }

    @Test
    public void should_get_all_spells() {}

    @Test
    public void should_get_by_name() {}

    @Test
    public void should_update_by_id() {}

    @Test
    public void should_delete_by_id() {}

    @Test
    public void should_get_by_id() {}

    @Test
    public void should_list_by_class() {}

    @Test
    public void should_list_by_level() {}

    @Test
    public void should_list_by_class_and_level() {}

    @Test
    public void should_throw_if_delete_by_non_existent_id() {}

    @Test
    public void should_throw_if_update_id_does_not_match() {}

    @Test
    public void should_throw_if_same_name_added() {}

    @Test
    public void should_throw_if_same_id_added() {}

    @Test
    public void should_return_null_if_get_by_non_existent_id() {}
}
