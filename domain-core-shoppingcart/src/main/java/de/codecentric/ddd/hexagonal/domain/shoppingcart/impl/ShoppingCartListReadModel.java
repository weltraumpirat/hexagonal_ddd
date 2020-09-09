package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;
import de.codecentric.ddd.hexagonal.domain.common.messaging.Messagebus;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCart;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItem;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartListRowRepository;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging.ShoppingCartCreatedEvent;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging.ShoppingCartDeletedEvent;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging.ShoppingCartUpdatedEvent;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.List;
import java.util.UUID;

public class ShoppingCartListReadModel {
  private final ShoppingCartListRowRepository repository;

  public ShoppingCartListReadModel( final ShoppingCartListRowRepository repository, final Messagebus eventbus ) {
    this.repository = repository;
    eventbus.register( ShoppingCartCreatedEvent.class, this::onCartCreated );
    eventbus.register( ShoppingCartDeletedEvent.class, this::onCartDeleted );
    eventbus.register( ShoppingCartUpdatedEvent.class, this::onCartUpdated );
  }


  public List<ShoppingCartListRow> read() {
    return repository.findAll();
  }

  private void onCartCreated( final Message<?> msg ) {
    final ShoppingCart cart = (( ShoppingCartCreatedEvent ) msg).getPayload();
    repository.create( rowFromCart( cart ) );
  }

  private void onCartDeleted( final Message<?> msg ) {
    final UUID cartId = ((ShoppingCartDeletedEvent) msg).getPayload();
    repository.delete( cartId );
  }

  public void onCartUpdated( final Message<?> msg ) {
    final ShoppingCart cart = ((ShoppingCartUpdatedEvent)msg).getPayload();
    repository.update( rowFromCart( cart ) );
  }

  private ShoppingCartListRow rowFromCart( final ShoppingCart cart ) {
    return new ShoppingCartListRow( cart.getId(),
                                    cart.getItems().size(),
                                    priceAsString( totalPrice( cart.getItems() ) ) );
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
}
