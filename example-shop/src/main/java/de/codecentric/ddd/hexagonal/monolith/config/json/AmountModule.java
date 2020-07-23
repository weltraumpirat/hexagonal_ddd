package de.codecentric.ddd.hexagonal.monolith.config.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.codecentric.ddd.hexagonal.monolith.domain.product.api.Amount;
import de.codecentric.ddd.hexagonal.monolith.domain.product.api.Fluid;

public class AmountModule extends SimpleModule {
  public AmountModule() {
    super("AmountModule");
    this.addSerializer( Fluid.class, new FluidSerializer());
    this.addDeserializer( Amount.class, new AmountDeserializer() );
  }
}
