package diacritics.owo.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.ibm.icu.impl.Pair;
import diacritics.owo.Ensign;
import net.minecraft.util.Identifier;

public class BannerShapesDeserializer implements JsonDeserializer<Pair<Boolean, List<Identifier>>> {
  @Override
  public Pair<Boolean, List<Identifier>> deserialize(JsonElement json, Type type,
      JsonDeserializationContext context)
      throws JsonParseException, IllegalStateException, NullPointerException {
    List<Identifier> identifiers = new ArrayList<>();

    JsonObject object = json.getAsJsonObject();
    boolean replace = object.getAsJsonPrimitive("replace").getAsBoolean();

    object.getAsJsonArray("values").forEach(element -> {
      Identifier id = Identifier.tryParse(element.getAsString());

      if (id != null) {
        identifiers.add(id);
      } else {
        Ensign.LOGGER.warn("received invalid banner shape identifier: " + element.getAsString());
      }
    });

    return Pair.of(replace, identifiers);
  }
}

