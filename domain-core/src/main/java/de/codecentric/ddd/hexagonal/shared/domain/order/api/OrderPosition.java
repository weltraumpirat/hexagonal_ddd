package de.codecentric.ddd.hexagonal.shared.domain.order.api;

import lombok.*;
import org.joda.money.Money;

import java.util.UUID;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PRIVATE, force = true)
public class OrderPosition {
  private final UUID id;
  private final String itemName;
  private final int count;
  private final Money singlePrice;
  private final Money combinedPrice;
}
