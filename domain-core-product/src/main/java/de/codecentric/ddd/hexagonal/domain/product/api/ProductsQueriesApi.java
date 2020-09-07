package de.codecentric.ddd.hexagonal.domain.product.api;

import java.util.List;

public interface ProductsQueriesApi {
  List<ProductListRow> getProductList();
  List<ProductShoppingListRow> getProductShoppingList();

  ProductValidationEntry validateProduct( String label );
}
