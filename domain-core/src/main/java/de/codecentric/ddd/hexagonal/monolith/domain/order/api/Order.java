package de.codecentric.ddd.hexagonal.monolith.domain.order.api;

import lombok.*;
import org.joda.money.Money;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor( access = AccessLevel.PRIVATE, force = true )
public class Order {
  private final UUID                id;
  private final Money               total;
  private final List<OrderPosition> positions;
  private final String       timestamp;
}
