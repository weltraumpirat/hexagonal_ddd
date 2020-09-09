package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ItemAndCartId {
  private final ShoppingCartItem item;
  private final UUID cartId;
}
