package de.codecentric.ddd.hexagonal.monolith.domain.product.api;

import lombok.*;
import org.joda.money.Money;

import java.util.UUID;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor( access = AccessLevel.PRIVATE, force = true )
public class Product {
  private final UUID          id;
  private final String        name;
  private final PackagingType packagingType;
  private final Money         price;
  private final Amount        amount;

  public String toLabel() {
    assert packagingType != null;
    return name+", "+packagingType.toString()+" "+amount;
  }
}
