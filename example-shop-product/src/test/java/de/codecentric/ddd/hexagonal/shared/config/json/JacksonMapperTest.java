package de.codecentric.ddd.hexagonal.shared.config.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import static de.codecentric.ddd.hexagonal.domain.product.api.Fluid.HALF_LITRE;
import de.codecentric.ddd.hexagonal.domain.product.api.PackagingType;
import de.codecentric.ddd.hexagonal.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.shared.config.ExampleShopProductConfig;
import static org.assertj.core.api.Assertions.assertThat;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

public class JacksonMapperTest {
  public static final ObjectMapper MAPPER = ExampleShopProductConfig.createObjectMapper();

  public static final Product PRODUCT    = new Product(
    UUID.fromString( "572e35e4-7be4-4c4a-bc49-32acd9aa09bc" ),
    "Whole Milk",
    PackagingType.CARTON,
    Money.of( CurrencyUnit.EUR, new BigDecimal( "1" ) ),
    HALF_LITRE );
  public static final String  JSON_VALUE = "{\"id\":\"572e35e4-7be4-4c4a-bc49-32acd9aa09bc\",\"name\":\"Whole Milk\",\"packagingType\":\"Carton\",\"price\":\"EUR 1.00\",\"amount\":\"0.5l\"}";

  @Test
  void shouldSerializeWithoutAnnotations() throws Exception {
    assertThat( MAPPER.writeValueAsString( PRODUCT ) ).isEqualTo( JSON_VALUE );
  }

  @Test
  void shouldDeserializeWithoutAnnotations() throws Exception {
    assertThat( MAPPER.readValue( JSON_VALUE, Product.class ) ).isEqualTo( PRODUCT );
  }
}
