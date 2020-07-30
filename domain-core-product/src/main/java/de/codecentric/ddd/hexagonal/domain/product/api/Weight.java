package de.codecentric.ddd.hexagonal.domain.product.api;

import java.util.Arrays;

public enum Weight implements Amount {
  QUARTER_KILO("250g"), HALF_KILO("500g"), KILO("1000g"), UNKNOWN("--");
  private final String amount;

  Weight( final String amount ) {
    this.amount = amount;
  }

  public static Amount forValue( final String amount ) {
    return Arrays.stream( Weight.values() )
      .filter( type -> type.toString().equals(amount) )
      .findAny().orElse(UNKNOWN);
  }

  @Override public String toString() {
    return amount;
  }
}
