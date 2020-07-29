package de.codecentric.ddd.hexagonal.shared.config.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.codecentric.ddd.hexagonal.shared.domain.product.api.PackagingType;

public class PackagingTypeModule extends SimpleModule {
  public PackagingTypeModule() {
    super("PackagingTypeModule");
    this.addSerializer( PackagingType.class, new PackagingTypeSerializer() );
    this.addDeserializer( PackagingType.class, new PackagingTypeDeserializer() );
  }
}
