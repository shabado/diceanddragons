package shabadoit.com.model;

import org.junit.Test;
import shabadoit.com.model.character.CharacterClass;
import shabadoit.com.model.character.ClassBlock;

import static org.junit.Assert.assertEquals;

public class ClassBlockTest {
    @Test
    public void should_level_up(){
        ClassBlock block = new ClassBlock(1);
        block.levelUp();

        assertEquals(2, block.getClassLevel());
    }
}
