package de.codecentric.ddd.hexagonal.shared.shoppingcart;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItem;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartNotFoundException;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartsApi;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.ShoppingCartItemsInfo;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.ShoppingCartListRow;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001","http://localhost" })
@RestController
public class ShoppingCartsController {
  private final ShoppingCartsApi api;

  public ShoppingCartsController(
    final ShoppingCartsApi api ) {
    this.api = api;
  }

  @GetMapping( "/api/cart" )
  public List<ShoppingCartListRow> getCarts() {
      return api.getShoppingCarts();
  }

  @GetMapping( "/api/cart/{cartId}" )
  public ShoppingCartItemsInfo getCartItems( @PathVariable final UUID cartId ) {
    try {
      return api.getShoppingCartItems( cartId );
    } catch( ShoppingCartNotFoundException e ) {
      throw shoppingCartNotFoundResponse( cartId, e );
    }
  }

  @PostMapping( "/api/cart" )
  public UUID createEmptyCart() {
    return api.createEmptyShoppingCart();
  }

  @DeleteMapping( "/api/cart/{cartId}" )
  @Transactional
  public void deleteCart( @PathVariable final UUID cartId ) {
    try {
      api.deleteCartById( cartId );
    } catch( ShoppingCartNotFoundException e ) {
      throw shoppingCartNotFoundResponse( cartId, e );
    }
  }

  @PostMapping( "/api/cart/{cartId}" )
  public void addItem( @PathVariable final UUID cartId, @RequestBody final ShoppingCartItem item ) {
    try {
      api.addItemToShoppingCart( cartId, item );
    } catch( NoSuchElementException e ) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "The provided item is not a valid product." );
    } catch( ShoppingCartNotFoundException e ) {
      throw shoppingCartNotFoundResponse( cartId, e );
    }
  }

  @DeleteMapping( "/api/cart/{cartId}/{itemId}" )
  public void removeItem( @PathVariable final UUID cartId, @PathVariable UUID itemId ) {
    try {
      api.removeItemFromShoppingCart( cartId, itemId );
    } catch( ShoppingCartNotFoundException e ) {
      throw shoppingCartNotFoundResponse( cartId, e );
    }
  }

  @PostMapping( "/api/cart/{cartId}/checkout" )
  @Transactional
  public UUID checkOutShoppingCart( @PathVariable final UUID cartId ) {
    try {
      return api.checkOut( cartId );
    } catch( ShoppingCartNotFoundException e ) {
      throw shoppingCartNotFoundResponse( cartId, e );
    }
  }

  private ResponseStatusException shoppingCartNotFoundResponse( final UUID cartId, ShoppingCartNotFoundException e ) {
    return new ResponseStatusException( HttpStatus.NOT_FOUND, "A shopping cart with id "+cartId+" does not exist.", e );
  }
}
