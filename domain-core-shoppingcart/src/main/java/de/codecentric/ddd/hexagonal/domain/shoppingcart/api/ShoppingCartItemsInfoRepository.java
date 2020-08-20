package de.codecentric.ddd.hexagonal.domain.shoppingcart.api;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.ShoppingCartItemsInfo;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartItemsInfoRepository {
  void create( UUID cartId, ShoppingCartItemsInfo info );

  void delete( UUID cartId );

  void update( UUID cartId, ShoppingCartItemsInfo info );

  List<ShoppingCartItemsInfo> findAll();

  ShoppingCartItemsInfo findById( UUID cartId );
}
