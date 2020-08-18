package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCart;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItem;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShoppingCartListReadModel {
  private final HashMap<UUID, ShoppingCartListRow> shoppingCarts;

  public ShoppingCartListReadModel() {
    shoppingCarts = new HashMap<>();
  }

  public List<ShoppingCartListRow> read() {
    return shoppingCarts.values().stream().collect( Collectors.toUnmodifiableList() );
  }

  public void handleCartCreated( final ShoppingCart cart ) {
    shoppingCarts.put( cart.getId(), rowFromCart( cart ) );
  }

  public void handleCartDeleted( final UUID id ) {
    shoppingCarts.remove( id );
  }

  public void handleCartUpdated( final ShoppingCart cart ) {
    shoppingCarts.put( cart.getId(), rowFromCart( cart ) );
  }
  private ShoppingCartListRow rowFromCart( final ShoppingCart cart) {
   final Money total = cart.getItems().stream()
                         .map( ShoppingCartItem::getPrice )
                         .reduce( ( final Money price, final Money sum)-> sum.plus( price ) )
                         .orElse( Money.zero( CurrencyUnit.EUR) );
    return new ShoppingCartListRow( cart.getId(), cart.getItems().size(), total.getCurrencyUnit()+" "+total.getAmount());
  }
}
