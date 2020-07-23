package de.codecentric.ddd.hexagonal.monolith.domain.product.api;

import java.util.Arrays;

public enum PackagingType {
  CARTON("Carton"), LOAF( "Loaf"), PACK( "Pack"), NONE("None");

  private final String packagingType;

  PackagingType( final String name ) {
    this.packagingType = name;
  }

  @Override public String toString() {
    return packagingType;
  }

  public static PackagingType forValue( final String name ) {
    return Arrays.stream( PackagingType.values() )
             .filter( type -> type.toString().equals(name) )
             .findAny().orElse(NONE);
  }
}
