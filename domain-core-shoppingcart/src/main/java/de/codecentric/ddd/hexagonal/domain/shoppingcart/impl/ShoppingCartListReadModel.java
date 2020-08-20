package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCart;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItem;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartListRowRepository;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.List;
import java.util.UUID;

public class ShoppingCartListReadModel {
  private final ShoppingCartListRowRepository repository;

  public ShoppingCartListReadModel( final ShoppingCartListRowRepository repository ) {
    this.repository = repository;
  }


  public List<ShoppingCartListRow> read() {
    return repository.findAll();
  }

  public void handleCartCreated( final ShoppingCart cart ) {
    repository.create( rowFromCart( cart ) );
  }

  public void handleCartDeleted( final UUID id ) {
    repository.delete( id );
  }

  public void handleCartUpdated( final ShoppingCart cart ) {
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
