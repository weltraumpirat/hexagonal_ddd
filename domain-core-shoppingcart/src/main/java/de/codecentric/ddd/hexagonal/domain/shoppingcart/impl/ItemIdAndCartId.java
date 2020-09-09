package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ItemIdAndCartId {
  private final UUID itemId;
  private final UUID cartId;
}
