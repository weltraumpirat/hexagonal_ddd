package de.codecentric.ddd.hexagonal.shoppingcart.persistence;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItemsInfoRepository;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.ShoppingCartItemsInfo;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShoppingCartItemsInfoRepositoryInMemory implements ShoppingCartItemsInfoRepository {
  private final HashMap<UUID, ShoppingCartItemsInfo> items;

  public ShoppingCartItemsInfoRepositoryInMemory() {
    items = new HashMap<>();
  }


  @Override public void create( final UUID cartId, final ShoppingCartItemsInfo info ) {
    persist( cartId, info );
  }

  private void persist( final UUID cartId, final ShoppingCartItemsInfo info ) {
    items.put( cartId, info );
  }

  @Override public void delete( final UUID cartId ) {
    items.remove( cartId );
  }

  @Override public void update( final UUID cartId, final ShoppingCartItemsInfo info ) {
    persist( cartId, info );
  }

  @Override public List<ShoppingCartItemsInfo> findAll() {
    return items.values().stream().collect( Collectors.toUnmodifiableList() );
  }

  @Override public ShoppingCartItemsInfo findById( final UUID cartId ) {
    return items.get( cartId );
  }
}
