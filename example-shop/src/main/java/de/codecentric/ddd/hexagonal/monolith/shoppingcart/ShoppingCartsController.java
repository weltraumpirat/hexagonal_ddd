package de.codecentric.ddd.hexagonal.monolith.shoppingcart;

import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.ShoppingCartNotFoundException;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api.ShoppingCartItem;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api.ShoppingCartsApi;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Log
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ShoppingCartsController {
  private final ShoppingCartsApi api;

  public ShoppingCartsController(
    final ShoppingCartsApi api ) {
    this.api = api;
  }

  @GetMapping( "/cart/{cartId}" )
  public List<ShoppingCartItem> getCartItems( @PathVariable final UUID cartId ) {
    try {
      return api.getShoppingCartItems( cartId );
    } catch( ShoppingCartNotFoundException e ) {
      throw shoppingCartNotFoundResponse( cartId, e );
    }
  }

  @PostMapping( "/cart" )
  public UUID createEmptyCart() {
    return api.createEmptyShoppingCart();
  }

  @PostMapping( "/cart/{cartId}" )
  public void addItem( @PathVariable final UUID cartId, @RequestBody final ShoppingCartItem item ) {
    try {
      final ShoppingCartItem itemToAdd = new ShoppingCartItem( item.getId() != null ? item.getId() : UUID.randomUUID(),item.getLabel(), item.getPrice() );
      api.addItemToShoppingCart( cartId, itemToAdd );
    } catch( NoSuchElementException e ) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "The provided item is not a valid product." );
    } catch( ShoppingCartNotFoundException e ) {
      throw shoppingCartNotFoundResponse( cartId, e );
    }
  }

  @DeleteMapping( "/cart/{cartId}/{itemId}" )
  public void removeItem( @PathVariable final UUID cartId, @PathVariable UUID itemId ) {
    try {
      api.removeItemFromShoppingCart( cartId, itemId );
    } catch( ShoppingCartNotFoundException e ) {
      throw shoppingCartNotFoundResponse( cartId, e );
    }
  }

  @PostMapping( "/cart/{cartId}/checkout" )
  @Transactional
  public void checkOutShoppingCart( @PathVariable final UUID cartId ) {
    try {
      api.checkOut( cartId );
    } catch( ShoppingCartNotFoundException e ) {
      throw shoppingCartNotFoundResponse( cartId, e );
    }
  }

  private ResponseStatusException shoppingCartNotFoundResponse( final UUID cartId, ShoppingCartNotFoundException e ) {
    return new ResponseStatusException( HttpStatus.NOT_FOUND, "A shopping cart with id "+cartId+" does not exist.", e );
  }
}
