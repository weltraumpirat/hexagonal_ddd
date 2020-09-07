package de.codecentric.ddd.hexagonal.domain.product.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Command;
import de.codecentric.ddd.hexagonal.domain.product.api.Product;

import java.util.UUID;

public class AddProductCommand extends Command<Product> {

  public static final String ADD_PRODUCT = "ADD_PRODUCT";

  public AddProductCommand( final Product payload ) {
    super( ADD_PRODUCT, payload );
  }

  public AddProductCommand( final UUID correlationId,
                            final Product payload ) {
    super( correlationId, ADD_PRODUCT, payload );
  }
}
