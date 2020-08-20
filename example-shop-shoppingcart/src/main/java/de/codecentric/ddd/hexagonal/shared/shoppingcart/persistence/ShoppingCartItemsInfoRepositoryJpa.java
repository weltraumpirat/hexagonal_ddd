package de.codecentric.ddd.hexagonal.shared.shoppingcart.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItem;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItemsInfoRepository;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.ShoppingCartItemsInfo;
import lombok.extern.java.Log;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log
public class ShoppingCartItemsInfoRepositoryJpa implements ShoppingCartItemsInfoRepository {
  private final ShoppingCartItemInfoCrudRepository crudRepository;
  private final ObjectMapper                       mapper;

  public ShoppingCartItemsInfoRepositoryJpa(
    final ShoppingCartItemInfoCrudRepository crudRepository, final ObjectMapper mapper ) {
    this.crudRepository = crudRepository;
    this.mapper = mapper;
  }

  @Override public void create( final UUID cartId, final ShoppingCartItemsInfo info ) {
    persist( cartId, info );
  }

  private void persist( final UUID cartId, final ShoppingCartItemsInfo info ) {
    try {
      final ShoppingCartItemsInfoEntity entity =
        new ShoppingCartItemsInfoEntity( cartId, mapper.writeValueAsString( info.getItems() ), info.getCount(),
                                         info.getTotal() );
      crudRepository.save( entity );
    } catch( JsonProcessingException e ) {
      log.info( "Error while serializing JSON: "+e.getMessage() );
    }
  }

  @Override public void delete( final UUID cartId ) {
    crudRepository.findById( cartId )
      .ifPresent( crudRepository::delete );
  }

  @Override public void update( final UUID cartId, final ShoppingCartItemsInfo info ) {
    persist( cartId, info );
  }

  @Override public ShoppingCartItemsInfo findById( final UUID cartId ) {
    return crudRepository.findById( cartId ).map( this::entityToInfo ).orElseThrow();
  }

  @Override public List<ShoppingCartItemsInfo> findAll() {
    return StreamSupport.stream( crudRepository.findAll().spliterator(), false )
             .map( this::entityToInfo )
             .collect( Collectors.toUnmodifiableList() );
  }

  private ShoppingCartItemsInfo entityToInfo( ShoppingCartItemsInfoEntity entity ) {
    try {
      @SuppressWarnings( "unchecked" ) final List<ShoppingCartItem> items =
        mapper.readValue( entity.getItems(), List.class );
      return new ShoppingCartItemsInfo( items, entity.getCount(), entity.getTotal() );
    } catch( JsonProcessingException e ) {
      log.info( "Error wile deserializing shoppingcart items list: "+e.getMessage() );
      return new ShoppingCartItemsInfo( Collections.emptyList(), 0, "-- --" );
    }
  }
}
