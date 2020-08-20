package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.*;

import java.util.List;
import java.util.UUID;

public class ShoppingCartsApiImpl implements ShoppingCartsApi {
  private final OrdersCheckoutPolicyService        ordersCheckoutPolicyService;
  private final ShoppingCartsCheckoutPolicyService shoppingCartsCheckoutPolicyService;
  private final ProductValidationService           productValidationService;
  private final ShoppingCartRepository             repository;
  private final ShoppingCartListReadModel          shoppingCartListReadModel;

  public ShoppingCartsApiImpl( final OrdersCheckoutPolicyService ordersCheckoutPolicyService,
                               final ProductValidationService productValidationService,
                               final ShoppingCartRepository repository,
                               final ShoppingCartListReadModel shoppingCartListReadModel ) {
    this.ordersCheckoutPolicyService = ordersCheckoutPolicyService;
    this.productValidationService = productValidationService;
    this.repository = repository;
    this.shoppingCartListReadModel = shoppingCartListReadModel;
    this.shoppingCartsCheckoutPolicyService = new ShoppingCartsCheckoutPolicyServiceInMemory( this );
  }

  @Override public ShoppingCart getShoppingCartById( final UUID cartId ) {
    return repository.findById( cartId );
  }

  @Override public UUID createEmptyShoppingCart() {
    ShoppingCart cart = ShoppingCartFactory.create();
    repository.create( cart );
    shoppingCartListReadModel.handleCartCreated( cart );
    return cart.getId();
  }

  @Override public void deleteCartById( final UUID cartId ) {
    repository.delete( cartId );
    shoppingCartListReadModel.handleCartDeleted( cartId );
  }

  @Override public void addItemToShoppingCart( final UUID cartId, final ShoppingCartItem item ) {
    final ShoppingCartItem itemToAdd = create( item );
    productValidationService.validate( itemToAdd );
    final ShoppingCartEntity cartEntity = ShoppingCartFactory.create( repository.findById( cartId ) );
    cartEntity.addItem( itemToAdd );
    update( cartEntity );
  }

  public ShoppingCartItem create( final ShoppingCartItem item ) {
    return ShoppingCartItemFactory.create(item);
  }

  private void update( final ShoppingCartEntity cartEntity ) {
    final ShoppingCart cart = ShoppingCartFactory.create( cartEntity );
    repository.update( cart );
    shoppingCartListReadModel.handleCartUpdated( cart );
  }

  @Override public void removeItemFromShoppingCart( final UUID cartId, final UUID itemId ) {
    final ShoppingCartEntity cart = ShoppingCartFactory.create( repository.findById( cartId ) );
    cart.removeItem( itemId );
    update(cart);
  }

  @Override public UUID checkOut( final UUID cartId ) {
    final ShoppingCartEntity cart = ShoppingCartFactory.create( repository.findById( cartId ) );
    ordersCheckoutPolicyService.invoke( cart.getItems() );
    return shoppingCartsCheckoutPolicyService.invoke( cartId );
  }

  @Override public List<ShoppingCartListRow> getShoppingCarts() {
    return shoppingCartListReadModel.read();
  }

  @Override public List<ShoppingCartItem> getShoppingCartItems( final UUID cartId ) {
    return repository.findById( cartId ).getItems();
  }
}

