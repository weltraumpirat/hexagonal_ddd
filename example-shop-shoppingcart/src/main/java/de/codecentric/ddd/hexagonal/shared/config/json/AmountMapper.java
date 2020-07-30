package de.codecentric.ddd.hexagonal.shared.config.json;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.Amount;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.Fluid;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.Weight;

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
