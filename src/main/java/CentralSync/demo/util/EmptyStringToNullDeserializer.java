package CentralSync.demo.util;

import CentralSync.demo.model.InventoryItemModule.ItemGroupEnum;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
public class EmptyStringToNullDeserializer extends JsonDeserializer<ItemGroupEnum> {
    @Override
    public ItemGroupEnum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText();
        if (text == null || text.isEmpty()) {
            return null;
        }
        return ItemGroupEnum.valueOf(text);
    }
}
