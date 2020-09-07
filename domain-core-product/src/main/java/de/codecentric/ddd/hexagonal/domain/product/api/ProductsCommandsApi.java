package de.codecentric.ddd.hexagonal.domain.product.api;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ProductsCommandsApi {
  CompletableFuture<Void> addProduct( Product product );

  CompletableFuture<Void> removeProduct( UUID id );
}
