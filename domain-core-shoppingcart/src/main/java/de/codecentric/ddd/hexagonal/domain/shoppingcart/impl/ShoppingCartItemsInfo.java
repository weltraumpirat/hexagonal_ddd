package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ShoppingCartItemsInfo {
  private final List<ShoppingCartItem> items;
  private final int count;
  private final String total;
}
