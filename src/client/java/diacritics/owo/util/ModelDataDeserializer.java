package diacritics.owo.util;

import java.lang.reflect.Type;
import java.util.stream.IntStream;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;

public class ModelDataDeserializer implements JsonDeserializer<ModelData> {
  @Override
  public ModelData deserialize(JsonElement json, Type type, JsonDeserializationContext context)
      throws JsonParseException, IllegalStateException, NullPointerException {
    ModelData data = new ModelData();

    JsonArray cuboids = json.getAsJsonArray();

    IntStream.range(0, cuboids.size()).forEach(i -> {
      ModelPartBuilder builder = ModelPartBuilder.create();

      JsonObject object = cuboids.get(i).getAsJsonObject();

      JsonArray offset = object.getAsJsonArray("offset");
      JsonArray size = object.getAsJsonArray("size");

      builder.uv(offset.get(0).getAsInt(), offset.get(1).getAsInt());

      builder.cuboid(offset.get(0).getAsFloat() - 10, offset.get(1).getAsFloat(),
          offset.get(2).getAsFloat() - 2, size.get(0).getAsFloat(), size.get(1).getAsFloat(),
          size.get(2).getAsFloat());

      data.getRoot().addChild(i + "", builder, ModelTransform.NONE);
    });

    return data;
  }
}
