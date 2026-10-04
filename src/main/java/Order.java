import java.util.List;

public record Order(
        long id,
        long customerId,
        OrderStatus status,
        List<OrderItem> items
) {}
