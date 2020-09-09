package de.codecentric.ddd.hexagonal.shared.shoppingcart.persistence;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.ShoppingCartListRow;
import static java.util.stream.Collectors.toUnmodifiableList;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class ShoppingCartListRowRepositoryJpa implements
  de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartListRowRepository {
  private final ShoppingCartListRowCrudRepository crudRepository;

  public ShoppingCartListRowRepositoryJpa(
    final ShoppingCartListRowCrudRepository crudRepository ) {
    this.crudRepository = crudRepository;
  }

  @Override public void create( final ShoppingCartListRow row ) {
    persist( row );
  }

  private void persist( final ShoppingCartListRow row ) {
    final ShoppingCartListRowEntity entity =
      new ShoppingCartListRowEntity( row.getId(), row.getCount(), row.getTotal() );
    crudRepository.save( entity );
  }

  @Override public void delete( final UUID id ) {
    crudRepository.deleteById( id );
  }

  @Override public void update( final ShoppingCartListRow row ) {
    persist( row );
  }

  @Override public List<ShoppingCartListRow> findAll() {
    return StreamSupport.stream( crudRepository.findAll().spliterator(), false )
             .map( e -> new ShoppingCartListRow( e.getId(), e.getCount(), e.getTotal() ) )
             .collect( toUnmodifiableList() );
  }
}
