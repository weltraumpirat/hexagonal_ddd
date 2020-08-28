package de.codecentric.ddd.hexagonal.shared.product.persistence;

import de.codecentric.ddd.hexagonal.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductListRow;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductValidationEntry;
import static de.codecentric.ddd.hexagonal.shared.config.json.AmountMapper.toAmount;
import static de.codecentric.ddd.hexagonal.shared.config.json.MoneyMapper.toMoney;

public class ProductFactory {
  public static ProductEntity create( final Product product ) {
    return new ProductEntity( product.getId(), product.getName(), product.getPackagingType(),
                              product.getPrice().getCurrencyUnit()
                              +" "+product.getPrice().getAmount(), product.getAmount().toString() );
  }

  public static ProductValidationEntity createValidationEntity( final ProductValidationEntry product ) {
    return new ProductValidationEntity( product.getId(), product.getLabel() );
  }

  public static ProductListRowEntity createListRowEntity( final ProductListRow product ) {
    return new ProductListRowEntity( product.getId(), product.getLabel(), product.getPrice() );
  }


  public static Product create( final ProductEntity entity ) {
    return new Product( entity.getId(),
                        entity.getName(),
                        entity.getPackagingType(),
                        toMoney( entity.getPrice() ),
                        toAmount( entity.getAmount() ) );
  }

  public static ProductValidationEntry create( final ProductValidationEntity entity ) {
    return new ProductValidationEntry( entity.getId(), entity.getLabel() );
  }

  public static ProductListRow create( final ProductListRowEntity entity ) {
    return new ProductListRow( entity.getId(),
                               entity.getLabel(),
                               entity.getPrice() );
  }
}
