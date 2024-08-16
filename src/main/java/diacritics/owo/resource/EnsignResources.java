package diacritics.owo.resource;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.icu.impl.Pair;
import diacritics.owo.Ensign;
import diacritics.owo.util.BannerShapesDeserializer;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class EnsignResources implements SimpleSynchronousResourceReloadListener {
  public static final Type BANNER_SHAPES_TYPE =
      new TypeToken<Pair<Boolean, List<Identifier>>>() {}.getType();

  public static final List<Identifier> BANNER_SHAPES = new ArrayList<>();
  public static final Gson gson = new GsonBuilder()
      .registerTypeAdapter(BANNER_SHAPES_TYPE, new BannerShapesDeserializer()).create();

  @Override
  public Identifier getFabricId() {
    return Ensign.identifier("data");
  }

  @Override
  public void reload(ResourceManager manager) {
    BANNER_SHAPES.clear();

    for (Identifier id : manager
        // TODO: way to specify only the path?
        .findResources("banner_shapes.json", path -> path.getPath().equals("banner_shapes.json"))
        .keySet()) {
      try (InputStream stream = manager.getResource(id).get().getInputStream()) {
        Pair<Boolean, List<Identifier>> shapes =
            gson.fromJson(new String(stream.readAllBytes()), BANNER_SHAPES_TYPE);

        if (shapes.first) {
          BANNER_SHAPES.clear();
        }

        BANNER_SHAPES.addAll(shapes.second);
      } catch (Exception e) {
        Ensign.LOGGER.error("encountered an error while loading banner shape " + id.toString(), e);
      }
    }

    Ensign.LOGGER.info("loaded " + BANNER_SHAPES.size() + " banner shapes: " + BANNER_SHAPES);
  }
}
