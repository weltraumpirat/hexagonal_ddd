package de.codecentric.ddd.hexagonal.monolith.domain.order.api;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PRIVATE, force = true)
public class Order {
  private final UUID                id;
  private final List<OrderPosition> positions;
}
