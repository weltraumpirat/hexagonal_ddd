package de.codecentric.ddd.hexagonal.monolith.config.json;

import de.codecentric.ddd.hexagonal.monolith.domain.product.api.Amount;
import de.codecentric.ddd.hexagonal.monolith.domain.product.api.Fluid;
import de.codecentric.ddd.hexagonal.monolith.domain.product.api.Weight;

public class AmountMapper {
  public static Amount toAmount( final String amount ) {
    switch( amount ) {
      case "0.5l":
      case "1l":
        return Fluid.forValue( amount );
      case "250g":
      case "500g":
      case "1000g":
        return Weight.forValue( amount );

    }
    return null;
  }
}
