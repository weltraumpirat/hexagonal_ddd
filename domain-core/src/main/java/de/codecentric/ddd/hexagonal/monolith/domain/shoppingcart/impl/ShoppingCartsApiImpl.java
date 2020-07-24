package de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api.ShoppingCart;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api.ShoppingCartItem;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api.ShoppingCartRepository;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api.ShoppingCartsApi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShoppingCartsApiImpl implements ShoppingCartsApi {
  private final ProductValidationService productValidationService;
  private final ShoppingCartRepository   repository;
  private final CheckoutService          checkoutService;

  public ShoppingCartsApiImpl( final CheckoutService checkoutService,
                               final ProductValidationService productValidationService,
                               final ShoppingCartRepository repository ) {
    this.checkoutService = checkoutService;
    this.productValidationService = productValidationService;
    this.repository = repository;
  }

  @Override public ShoppingCart getShoppingCartById( final UUID cartId ) {
    return repository.findById( cartId );
  }

  @Override public UUID createEmptyShoppingCart() {
    ShoppingCart cart = new ShoppingCart( UUID.randomUUID(), new ArrayList<>() );
    repository.create( cart );
    return cart.getId();
  }

  @Override public void addItemToShoppingCart( final UUID cartId, final ShoppingCartItem shoppingCartItem ) {
    productValidationService.validate( shoppingCartItem );
    final ShoppingCartEntity cart = ShoppingCartFactory.create( repository.findById( cartId ) );
    cart.addItem( shoppingCartItem );
    repository.update( ShoppingCartFactory.create( cart ) );
  }

  @Override public void removeItemFromShoppingCart( final UUID cartId, final UUID itemId ) {
    final ShoppingCartEntity cart = ShoppingCartFactory.create( repository.findById( cartId ) );
    cart.removeItem( itemId );
    repository.update( ShoppingCartFactory.create( cart ) );
  }

  @Override public void checkOut( final UUID cartId ) {
    final ShoppingCartEntity cart = ShoppingCartFactory.create( repository.findById( cartId ) );
    checkoutService.checkOut( cart.getItems() );
    repository.delete( cartId );
  }

  @Override public List<ShoppingCartItem> getShoppingCartItems( final UUID cartId ) {
    return repository.findById( cartId ).getItems();
  }
}
