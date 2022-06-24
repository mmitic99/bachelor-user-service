package bachelor.UserService.config.mongoDB;


/*import bachelor.UserService.model.Key;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.*;

public class KeyDeserializer implements JsonDeserializer<Key> {
    private String keyTypeElementName;
    private Gson gson;
    private Map<String, Class<? extends Key>> keyTypeRegistry;

    public KeyDeserializer(String keyTypeElementName) {
        this.keyTypeElementName = keyTypeElementName;
        this.gson = new Gson();
        this.keyTypeRegistry = new HashMap<>();
    }

    public void registerKeyType(String keyTypeName, Class<? extends Key> keyType) {
        keyTypeRegistry.put(keyTypeName, keyType);
    }

    public Key deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject keyObject = json.getAsJsonObject();
        JsonElement keyTypeElement = keyObject.get(keyTypeElementName);

        Class<? extends Key> keyType = keyTypeRegistry.get(keyTypeElement.getAsString());
        return gson.fromJson(keyObject, keyType);
    }
}
*/