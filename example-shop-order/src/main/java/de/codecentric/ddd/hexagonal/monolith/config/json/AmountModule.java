package de.codecentric.ddd.hexagonal.monolith.config.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.codecentric.ddd.hexagonal.monolith.domain.product.api.Amount;
import de.codecentric.ddd.hexagonal.monolith.domain.product.api.Fluid;
import de.codecentric.ddd.hexagonal.monolith.domain.product.api.Weight;

public class AmountModule extends SimpleModule {
  public AmountModule() {
    super("AmountModule");
    this.addSerializer( Fluid.class, new FluidSerializer());
    this.addSerializer( Weight.class, new WeightSerializer() );
    this.addDeserializer( Amount.class, new AmountDeserializer() );
  }
}
