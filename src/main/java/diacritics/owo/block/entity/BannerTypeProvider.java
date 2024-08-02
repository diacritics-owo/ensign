package diacritics.owo.block.entity;

import diacritics.owo.component.type.BannerTypeComponent;

public interface BannerTypeProvider {
  public void setBannerType(BannerTypeComponent bannerType);

  public BannerTypeComponent getBannerType();
}
