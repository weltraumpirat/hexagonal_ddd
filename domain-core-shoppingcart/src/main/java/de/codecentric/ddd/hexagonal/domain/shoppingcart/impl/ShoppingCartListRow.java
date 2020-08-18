package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ShoppingCartListRow {
  private final UUID id;
  private final int count;
  private final String total;
}
