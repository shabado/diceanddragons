package shabadoit.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import shabadoit.model.character.CharacterSheet;
import shabadoit.model.stats.SaveBlock;
import shabadoit.model.stats.SkillBlock;

import java.io.IOException;

public class CharacterSheetDeserializer extends StdDeserializer<CharacterSheet> {

    public CharacterSheetDeserializer() {
        super(CharacterSheet.class);
    }

    @Override
    public CharacterSheet deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        CharacterSheet deserializedCharacter = null;

        DeserializationConfig config = ctxt.getConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(CharacterSheet.class);

        JsonDeserializer<Object> defaultDeserializer = BeanDeserializerFactory.instance.buildBeanDeserializer(ctxt, type, config.introspect(type));

        if (defaultDeserializer instanceof ResolvableDeserializer) {
            ((ResolvableDeserializer) defaultDeserializer).resolve(ctxt);
        }

        JsonParser treeParser = oc.treeAsTokens(node);
        config.initialize(treeParser);

        if (treeParser.getCurrentToken() == null) {
            treeParser.nextToken();
        }

        deserializedCharacter = (CharacterSheet) defaultDeserializer.deserialize(treeParser, ctxt);

        if (deserializedCharacter.getSaves() == null) {
            deserializedCharacter.setSaves(new SaveBlock());
        }
        if (deserializedCharacter.getSkills() == null) {
            deserializedCharacter.setSkills(new SkillBlock());
        }

        return deserializedCharacter;
    }
}