package de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class ShoppingCart {
  private final UUID id;
  private final List<ShoppingCartItem> items;
}
