package de.codecentric.ddd.hexagonal.domain.order.api;

import java.util.Arrays;

public enum Fluid implements Amount {
  HALF_LITRE("0.5l"), LITRE("1l"), UNKNOWN("--");
  private final String amount;

  Fluid( final String amount ) {
    this.amount = amount;
  }

  public static Amount forValue( final String amount ) {
    return Arrays.stream( Fluid.values() )
      .filter( type -> type.toString().equals(amount) )
      .findAny().orElse(UNKNOWN);
  }

  @Override public String toString() {
    return amount;
  }
}
