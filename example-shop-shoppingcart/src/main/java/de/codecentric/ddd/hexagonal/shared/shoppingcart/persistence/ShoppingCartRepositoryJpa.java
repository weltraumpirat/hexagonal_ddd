package de.codecentric.ddd.hexagonal.shared.shoppingcart.persistence;

import static de.codecentric.ddd.hexagonal.shared.config.json.MoneyMapper.toMoney;
import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.ShoppingCartNotFoundException;
import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.ShoppingCart;
import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.ShoppingCartItem;
import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.ShoppingCartRepository;
import org.joda.money.Money;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ShoppingCartRepositoryJpa implements ShoppingCartRepository {
  private final ShoppingCartItemCrudRepository jpaItemsRepo;
  private final ShoppingCartCrudRepository     jpaCartsRepo;

  public ShoppingCartRepositoryJpa(
    final ShoppingCartCrudRepository jpaCartsRepo,
    final ShoppingCartItemCrudRepository itemsRepo ) {
    this.jpaCartsRepo = jpaCartsRepo;
    this.jpaItemsRepo = itemsRepo;
  }


  @Override public void create( final ShoppingCart cart ) {
    save( cart );
  }

  @Override public void update( final ShoppingCart shoppingCart ) {
    save( shoppingCart );
  }

  private void save( final ShoppingCart cart ) {
    final UUID cartId = cart.getId();
    jpaCartsRepo.save( new ShoppingCartEntity( cartId ) );

    final List<ShoppingCartItemEntity> entities = cart.getItems().stream()
                                                    .map( item -> itemToEntity( cartId, item ) )
                                                    .collect( Collectors.toList() );
    jpaItemsRepo.saveAll( entities );
  }

  private ShoppingCartItemEntity itemToEntity( final UUID cartId, final ShoppingCartItem item ) {
    final ShoppingCartItemEntity entity =
      new ShoppingCartItemEntity();
    entity.setId( item.getId() );
    entity.setLabel( item.getLabel() );
    final Money money = item.getPrice();
    entity.setPrice( money.getCurrencyUnit()+" "+money.getAmount() );
    entity.setCartId( cartId );
    return entity;
  }

  @Override public ShoppingCart findById( final UUID cartId ) {
    if( jpaCartsRepo.existsById( cartId ) ) {
      final Iterable<ShoppingCartItemEntity> entities = jpaItemsRepo.findByCartId( cartId );
      final List<ShoppingCartItem> items = StreamSupport.stream( entities.spliterator(), false )
                                             .map( e -> new ShoppingCartItem( e.getId(),
                                                                              e.getLabel(),
                                                                              toMoney( e.getPrice() ) ) )
                                             .collect( Collectors.toList() );
      return new ShoppingCart( cartId, items );
    } else throw new ShoppingCartNotFoundException();
  }

  @Override public List<ShoppingCart> findAll() {
    Map<UUID, List<ShoppingCartItem>> items = new HashMap<>();
    jpaItemsRepo.findAll().forEach( e->{
      List<ShoppingCartItem> list = items.getOrDefault( e.getCartId(), new ArrayList<>() );
      list.add(new ShoppingCartItem( e.getId(),
                                     e.getLabel(),
                                     toMoney( e.getPrice() ) ) );
      items.put(e.getCartId(), list);
    } );
    return StreamSupport.stream( jpaCartsRepo.findAll().spliterator(), false )
             .map( c -> new ShoppingCart( c.getId(), items.getOrDefault( c.getId(), Collections.emptyList() ) ) )
             .collect( Collectors.toUnmodifiableList() );
  }

  @Override public void delete( final UUID cartId ) {
    deleteCartItems( cartId );
    deleteCart( cartId );
  }

  public void deleteCart( final UUID cartId ) {
    jpaCartsRepo.deleteById( cartId );
  }

  public void deleteCartItems( final UUID cartId ) {
    jpaItemsRepo.deleteAllByCartId( cartId );
  }
}
