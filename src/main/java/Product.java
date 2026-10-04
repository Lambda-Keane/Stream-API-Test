import java.math.BigDecimal;

public record Product(long id,
                      String name,
                      Category category,
                      BigDecimal price,
                      int stock) {
}
