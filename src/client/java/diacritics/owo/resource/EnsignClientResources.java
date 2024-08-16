package diacritics.owo.resource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diacritics.owo.Ensign;
import diacritics.owo.util.ModelDataDeserializer;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class EnsignClientResources implements SimpleSynchronousResourceReloadListener {
  public static final Map<Identifier, TexturedModelData> BANNER_SHAPES = new HashMap<>();
  public static final Gson gson =
      new GsonBuilder().registerTypeAdapter(ModelData.class, new ModelDataDeserializer()).create();

  @Override
  public Identifier getFabricId() {
    return Ensign.identifier("resources");
  }

  @Override
  public void reload(ResourceManager manager) {
    BANNER_SHAPES.clear();

    for (Identifier id : manager
        .findResources("banner_shapes", path -> path.getPath().endsWith(".json")).keySet()) {
      try (InputStream stream = manager.getResource(id).get().getInputStream()) {
        BANNER_SHAPES.put(
            Identifier.of(id.getNamespace(),
                id.getPath().replaceFirst(".json$", "").replaceFirst("banner_shapes/", "")),
            TexturedModelData.of(gson.fromJson(new String(stream.readAllBytes()), ModelData.class),
                64, 64));
      } catch (Exception e) {
        Ensign.LOGGER.error("encountered an error while loading banner shape " + id.toString(), e);
      }
    }

    Ensign.LOGGER
        .info("loaded " + BANNER_SHAPES.size() + " banner models: " + BANNER_SHAPES.keySet());
  }
}
