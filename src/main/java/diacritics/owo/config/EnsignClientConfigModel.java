package diacritics.owo.config;

import diacritics.owo.Ensign;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;

@Modmenu(modId = Ensign.MOD_ID)
@Config(name = "ensign-client-config", wrapperName = "EnsignClientConfig")
public class EnsignClientConfigModel {
  public boolean animateBanners = true;
}
