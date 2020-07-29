package de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.shared.domain.order.impl.OrdersCheckoutPolicyService;
import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShoppingCartsApiImpl implements ShoppingCartsApi {
  private final OrdersCheckoutPolicyService        ordersCheckoutPolicyService;
  private final ShoppingCartsCheckoutPolicyService shoppingCartsCheckoutPolicyService;
  private final ProductValidationService           productValidationService;
  private final ShoppingCartRepository             repository;

  public ShoppingCartsApiImpl( final OrdersCheckoutPolicyService ordersCheckoutPolicyService,
                               final ProductValidationService productValidationService,
                               final ShoppingCartRepository repository ) {
    this.ordersCheckoutPolicyService = ordersCheckoutPolicyService;
    this.productValidationService = productValidationService;
    this.repository = repository;
    this.shoppingCartsCheckoutPolicyService = new ShoppingCartsCheckoutPolicyService( this );
  }

  @Override public ShoppingCart getShoppingCartById( final UUID cartId ) {
    return repository.findById( cartId );
  }

  @Override public UUID createEmptyShoppingCart() {
    ShoppingCart cart = new ShoppingCart( UUID.randomUUID(), new ArrayList<>() );
    repository.create( cart );
    return cart.getId();
  }

  @Override public void deleteCartById( final UUID cartId ) {
    repository.delete( cartId );
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

  @Override public UUID checkOut( final UUID cartId ) {
    final ShoppingCartEntity cart = ShoppingCartFactory.create( repository.findById( cartId ) );
    ordersCheckoutPolicyService.invoke( cart.getItems() );
    return shoppingCartsCheckoutPolicyService.invoke( cartId );
  }

  @Override public List<ShoppingCart> getShoppingCarts() {
    return repository.findAll();
  }

  @Override public List<ShoppingCartItem> getShoppingCartItems( final UUID cartId ) {
    return repository.findById( cartId ).getItems();
  }
}
