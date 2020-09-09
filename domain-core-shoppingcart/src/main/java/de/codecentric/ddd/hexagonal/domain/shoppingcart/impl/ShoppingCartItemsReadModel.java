package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;
import de.codecentric.ddd.hexagonal.domain.common.messaging.Messagebus;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCart;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItem;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItemsInfoRepository;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging.ShoppingCartCreatedEvent;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging.ShoppingCartDeletedEvent;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging.ShoppingCartUpdatedEvent;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ShoppingCartItemsReadModel {
  private final ShoppingCartItemsInfoRepository repository;

  public ShoppingCartItemsReadModel(
    final ShoppingCartItemsInfoRepository repository, final Messagebus eventbus ) {
    this.repository = repository;
    eventbus.register(ShoppingCartCreatedEvent.class, this::onCartCreated);
    eventbus.register( ShoppingCartDeletedEvent.class, this::onCartDeleted );
    eventbus.register( ShoppingCartUpdatedEvent.class, this::onCartUpdated );
  }

  public ShoppingCartItemsInfo read( final UUID cartId ) {
    return repository.findById( cartId );
  }

  public void onCartUpdated( final Message<?> msg ) {
    final ShoppingCart cart = ((ShoppingCartUpdatedEvent)msg).getPayload();
    repository.update( cart.getId(), new ShoppingCartItemsInfo( cart.getItems(),
                                                                cart.getItems().size(),
                                                                priceAsString( totalPrice( cart.getItems() ) ) ) );
  }

  private Money totalPrice( List<ShoppingCartItem> items ) {
    return items.stream()
             .map( ShoppingCartItem::getPrice )
             .reduce( ( final Money price, final Money sum ) -> sum.plus( price ) )
             .orElse( Money.zero( CurrencyUnit.EUR ) );
  }

  private String priceAsString( final Money money ) {
    return money.getCurrencyUnit()+" "+
           money.getAmount();
  }

  private void onCartDeleted( final Message<?> msg ) {
    final UUID cartId = ((ShoppingCartDeletedEvent) msg).getPayload();
    repository.delete( cartId );
  }

  private void onCartCreated( final Message<?> msg ) {
    final ShoppingCart cart = ((ShoppingCartCreatedEvent) msg).getPayload();
    repository.create( cart.getId(),
                       new ShoppingCartItemsInfo( Collections.emptyList(), 0,
                                                  priceAsString( totalPrice( cart.getItems() ) ) ) );
  }
}
