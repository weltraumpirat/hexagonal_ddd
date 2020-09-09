package de.codecentric.ddd.hexagonal.domain.product.api;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ProductsCommandsApi {
  CompletableFuture<Product> addProduct( Product product );

  CompletableFuture<UUID> removeProduct( UUID id );
}
