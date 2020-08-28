package de.codecentric.ddd.hexagonal.domain.product.api;

import java.util.List;
import java.util.UUID;

public interface ProductsQueriesApi {
  List<Product> getProducts();
  List<ProductListRow> getProductList();
  List<ProductShoppingListRow> getProductShoppingList();

  Product getProductById( UUID id );

  ProductValidationEntry validateProduct( String label );
}
