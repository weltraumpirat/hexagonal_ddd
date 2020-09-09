package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;
import de.codecentric.ddd.hexagonal.domain.common.messaging.Messagebus;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.*;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging.*;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ShoppingCartFixture {
  private final ShoppingCartRepository   repository;
  private final ProductValidationService productValidationService;
  private final OrdersCheckoutPolicyService
                                         ordersCheckoutPolicyService;
  private final ShoppingCartsCheckoutPolicyService
                                         shoppingCartsCheckoutPolicyService;
  private final Messagebus               eventbus;

  public ShoppingCartFixture( final ShoppingCartRepository repository,
                              final ProductValidationService productValidationService,
                              final OrdersCheckoutPolicyService ordersCheckoutPolicyService,
                              final ShoppingCartsCheckoutPolicyService shoppingCartsCheckoutPolicyService,
                              final Messagebus eventbus,
                              final Messagebus commandbus ) {
    this.repository = repository;
    this.productValidationService = productValidationService;
    this.eventbus = eventbus;
    this.ordersCheckoutPolicyService = ordersCheckoutPolicyService;
    this.shoppingCartsCheckoutPolicyService = shoppingCartsCheckoutPolicyService;
    commandbus.register( CreateShoppingCartCommand.class, this::onCreateShoppingCart );
    commandbus.register( DeleteShoppingCartCommand.class, this::onDeleteShoppingCart );
    commandbus.register( AddItemToShoppingCartCommand.class, this::onAddItemToShoppingCart );
    commandbus.register( RemoveItemFromShoppingCartCommand.class, this::onRemoveItemFromShoppingCart );
    commandbus.register( CheckOutShoppingCartCommand.class, this::onCheckOutShoppingCart );
  }

  public void onCreateShoppingCart( final Message<?> msg ) {
    final ShoppingCart cart = ShoppingCartFactory.create();
    repository.create( cart );
    msg.getCorrelationId().ifPresentOrElse(
      correlationId -> eventbus.send( new ShoppingCartCreatedEvent( correlationId, cart ) ),
      () -> eventbus.send( new ShoppingCartCreatedEvent( cart ) ) );
  }

  private void onDeleteShoppingCart( final Message<?> msg ) {
    final UUID cartId = ( (DeleteShoppingCartCommand) msg ).getPayload();
    repository.delete( cartId );
    msg.getCorrelationId().ifPresentOrElse(
      correlationId -> eventbus.send( new ShoppingCartDeletedEvent( correlationId, cartId ) ),
      () -> eventbus.send( new ShoppingCartDeletedEvent( cartId ) ) );
  }

  private void onAddItemToShoppingCart( final Message<?> msg ) {
    final ItemAndCartId itemAndCartId = ( (AddItemToShoppingCartCommand) msg ).getPayload();
    final ShoppingCartItem itemToAdd = ShoppingCartItemFactory.create( itemAndCartId.getItem() );
    productValidationService.validate( itemToAdd );
    final ShoppingCartEntity cartEntity = getShoppingCartById( itemAndCartId.getCartId() );
    cartEntity.addItem( itemToAdd );
    update( msg.getCorrelationId(), cartEntity );
  }

  private void onRemoveItemFromShoppingCart( final Message<?> msg ) {
    final ItemIdAndCartId itemIdAndCartId = ( (RemoveItemFromShoppingCartCommand) msg ).getPayload();
    final ShoppingCartEntity cart = getShoppingCartById( itemIdAndCartId.getCartId() );
    cart.removeItem( itemIdAndCartId.getItemId() );
    update( msg.getCorrelationId(), cart );
  }

  private ShoppingCartEntity getShoppingCartById( final UUID cartId ) {
    return ShoppingCartFactory.create( repository.findById( cartId ) );
  }

  private void update( final Optional<UUID> correlationId, final ShoppingCartEntity cartEntity ) {
    final ShoppingCart cart = ShoppingCartFactory.create( cartEntity );
    repository.update( cart );
    correlationId.ifPresentOrElse(
      c -> eventbus.send( new ShoppingCartUpdatedEvent( c, cart ) ),
      () -> eventbus.send( new ShoppingCartUpdatedEvent( cart ) ) );

  }

  private void onCheckOutShoppingCart( final Message<?> msg ) {
    final UUID cartId = ( (CheckOutShoppingCartCommand) msg ).getPayload();
    final ShoppingCartEntity cart = getShoppingCartById( cartId );
    ordersCheckoutPolicyService.invoke( cart.getItems() );
    final UUID correlationId = msg.getCorrelationId().orElseThrow();
    final UUID newCartId;
    try {
      newCartId = shoppingCartsCheckoutPolicyService.invoke( correlationId, cartId ).get();
      eventbus.send( new ShoppingCartCheckedOutEvent( correlationId, newCartId ) );
    } catch( InterruptedException|ExecutionException e ) {
      throw new RuntimeException( e.getMessage() );
    }
  }
}
