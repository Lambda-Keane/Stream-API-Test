import java.math.BigDecimal;
import java.util.List;

public class SampleData {

    // ---------- PRODUCTS ----------

    public static final Product IPHONE_15 =
            new Product(1, "iPhone 15", Category.PHONE,
                    new BigDecimal("799.00"), 20);

    public static final Product GALAXY_S24 =
            new Product(2, "Galaxy S24", Category.PHONE,
                    new BigDecimal("749.00"), 15);

    public static final Product PIXEL_9 =
            new Product(3, "Pixel 9", Category.PHONE,
                    new BigDecimal("699.00"), 10);

    public static final Product IPAD_AIR =
            new Product(4, "iPad Air", Category.TABLET,
                    new BigDecimal("599.00"), 12);

    public static final Product GALAXY_TAB =
            new Product(5, "Galaxy Tab S9", Category.TABLET,
                    new BigDecimal("649.00"), 8);

    public static final Product MACBOOK_AIR =
            new Product(6, "MacBook Air", Category.LAPTOP,
                    new BigDecimal("1099.00"), 7);

    public static final Product DELL_XPS =
            new Product(7, "Dell XPS 13", Category.LAPTOP,
                    new BigDecimal("1299.00"), 5);

    public static final Product LENOVO_LEGION =
            new Product(8, "Lenovo Legion 5", Category.LAPTOP,
                    new BigDecimal("1199.00"), 6);

    public static final Product AIRPODS_PRO =
            new Product(9, "AirPods Pro", Category.HEADPHONES,
                    new BigDecimal("249.00"), 30);

    public static final Product SONY_WH1000XM5 =
            new Product(10, "Sony WH-1000XM5", Category.HEADPHONES,
                    new BigDecimal("349.00"), 18);

    public static final Product BOSE_QC =
            new Product(11, "Bose QuietComfort", Category.HEADPHONES,
                    new BigDecimal("299.00"), 14);

    public static final Product BUDS_3 =
            new Product(12, "Galaxy Buds 3", Category.HEADPHONES,
                    new BigDecimal("179.00"), 25);


    public static final List<Product> PRODUCTS = List.of(
            IPHONE_15,
            GALAXY_S24,
            PIXEL_9,
            IPAD_AIR,
            GALAXY_TAB,
            MACBOOK_AIR,
            DELL_XPS,
            LENOVO_LEGION,
            AIRPODS_PRO,
            SONY_WH1000XM5,
            BOSE_QC,
            BUDS_3
    );


    // ---------- CUSTOMERS ----------

    public static final Customer ALICE =
            new Customer(1, "Alice");

    public static final Customer BOB =
            new Customer(2, "Bob");

    public static final Customer CHARLIE =
            new Customer(3, "Charlie");

    public static final Customer DAVID =
            new Customer(4, "David");

    public static final Customer EMMA =
            new Customer(5, "Emma");

    public static final Customer FRANK =
            new Customer(6, "Frank");

    public static final List<Customer> CUSTOMERS = List.of(
            ALICE,
            BOB,
            CHARLIE,
            DAVID,
            EMMA,
            FRANK
    );


    // ---------- ORDERS ----------

    public static final Order ORDER_101 =
            new Order(
                    101,
                    1,
                    OrderStatus.DELIVERED,
                    List.of(
                            new OrderItem(IPHONE_15, 1),
                            new OrderItem(AIRPODS_PRO, 1)
                    )
            );

    public static final Order ORDER_102 =
            new Order(
                    102,
                    1,
                    OrderStatus.DELIVERED,
                    List.of(
                            new OrderItem(MACBOOK_AIR, 1),
                            new OrderItem(SONY_WH1000XM5, 1)
                    )
            );

    public static final Order ORDER_103 =
            new Order(
                    103,
                    2,
                    OrderStatus.SHIPPED,
                    List.of(
                            new OrderItem(GALAXY_S24, 2),
                            new OrderItem(BUDS_3, 1)
                    )
            );

    public static final Order ORDER_104 =
            new Order(
                    104,
                    2,
                    OrderStatus.DELIVERED,
                    List.of(
                            new OrderItem(GALAXY_TAB, 1),
                            new OrderItem(BUDS_3, 2)
                    )
            );

    public static final Order ORDER_105 =
            new Order(
                    105,
                    3,
                    OrderStatus.PAID,
                    List.of(
                            new OrderItem(PIXEL_9, 1),
                            new OrderItem(AIRPODS_PRO, 2)
                    )
            );

    public static final Order ORDER_106 =
            new Order(
                    106,
                    3,
                    OrderStatus.DELIVERED,
                    List.of(
                            new OrderItem(DELL_XPS, 1)
                    )
            );

    public static final Order ORDER_107 =
            new Order(
                    107,
                    4,
                    OrderStatus.CANCELLED,
                    List.of(
                            new OrderItem(IPHONE_15, 1),
                            new OrderItem(AIRPODS_PRO, 1)
                    )
            );

    public static final Order ORDER_108 =
            new Order(
                    108,
                    4,
                    OrderStatus.SHIPPED,
                    List.of(
                            new OrderItem(LENOVO_LEGION, 1),
                            new OrderItem(SONY_WH1000XM5, 1)
                    )
            );

    public static final Order ORDER_109 =
            new Order(
                    109,
                    5,
                    OrderStatus.DELIVERED,
                    List.of(
                            new OrderItem(IPAD_AIR, 1),
                            new OrderItem(GALAXY_S24, 1),
                            new OrderItem(BUDS_3, 1)
                    )
            );

    public static final Order ORDER_110 =
            new Order(
                    110,
                    5,
                    OrderStatus.PENDING,
                    List.of(
                            new OrderItem(MACBOOK_AIR, 1)
                    )
            );

    public static final Order ORDER_111 =
            new Order(
                    111,
                    6,
                    OrderStatus.DELIVERED,
                    List.of(
                            new OrderItem(IPHONE_15, 2),
                            new OrderItem(GALAXY_TAB, 1)
                    )
            );

    public static final Order ORDER_112 =
            new Order(
                    112,
                    6,
                    OrderStatus.PAID,
                    List.of(
                            new OrderItem(BOSE_QC, 2)
                    )
            );


    public static final List<Order> ORDERS = List.of(
            ORDER_101,
            ORDER_102,
            ORDER_103,
            ORDER_104,
            ORDER_105,
            ORDER_106,
            ORDER_107,
            ORDER_108,
            ORDER_109,
            ORDER_110,
            ORDER_111,
            ORDER_112
    );
}
