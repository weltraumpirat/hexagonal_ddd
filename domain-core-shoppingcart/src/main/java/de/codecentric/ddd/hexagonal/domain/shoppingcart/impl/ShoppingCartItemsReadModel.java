package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCart;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItem;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItemsInfoRepository;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ShoppingCartItemsReadModel {
  private final ShoppingCartItemsInfoRepository repository;

  public ShoppingCartItemsReadModel(
    final ShoppingCartItemsInfoRepository repository ) {
    this.repository = repository;
  }

  public ShoppingCartItemsInfo read( final UUID cartId ) {
    return repository.findById( cartId );
  }

  public void handleCartUpdated( final ShoppingCart cart ) {
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

  public void handleCartDeleted( final UUID cartId ) {
    repository.delete( cartId );
  }

  public void handleCartCreated( final ShoppingCart cart ) {
    repository.create( cart.getId(),
                       new ShoppingCartItemsInfo( Collections.emptyList(), 0,
                                                  priceAsString( totalPrice( cart.getItems() ) ) ) );
  }
}
