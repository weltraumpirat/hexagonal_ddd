package de.codecentric.ddd.hexagonal.shoppingcart.persistence;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartListRowRepository;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.ShoppingCartListRow;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShoppingCartListRowRepositoryInMemory implements ShoppingCartListRowRepository {
  private final HashMap<UUID, ShoppingCartListRow> rows;

  public ShoppingCartListRowRepositoryInMemory() {
    rows = new HashMap<>();
  }

  @Override public void create( final ShoppingCartListRow row ) {
    persist( row );
  }

  private void persist( final ShoppingCartListRow row ) {
    rows.put( row.getId(), row );
  }

  @Override public void delete( final UUID id ) {
    rows.remove( id );
  }

  @Override public void update( final ShoppingCartListRow row ) {
    persist( row );
  }

  @Override public List<ShoppingCartListRow> findAll() {
    return rows.values().stream().collect( Collectors.toUnmodifiableList() );
  }
}
