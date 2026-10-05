import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class ProductAnalytics {

    private final List<Product> products = SampleData.PRODUCTS;
    private final List<Order> orders = SampleData.ORDERS;

//*************************************************************
// FIRST TEST
//*************************************************************

    // ============================================================
    // EASY 1 — Filter + Map
    // ============================================================

    /**
     * Find the names of products that:
     * <p>
     * - belong to the given category
     * <p>
     * - cost more than the given minimum price
     * <p>
     * Return the product names.
     */
    public List<String> findProductsByCategoryAndMinPrice(
            List<Product> products,
            Category category,
            BigDecimal minimumPrice
    ) {
        return products.stream()
                .filter(p -> p.category() == category)
                .filter(p -> p.price().compareTo(minimumPrice) > 0)
                .map(Product::name)
                .toList();
    }


    // ============================================================
    // EASY 2 — Sort + Limit
    // ============================================================

    /**
     * Return the N cheapest products.
     * <p>
     * Products should be sorted from cheapest to most expensive.
     */
    public List<Product> findCheapestProducts(
            List<Product> products,
            int limit
    ) {
        return products.stream()
                .sorted(Comparator.comparing(Product::price, Comparator.naturalOrder()))
                .limit(limit)
                .toList();
    }


    // ============================================================
    // EASY 3 — Aggregation
    // ============================================================

    /**
     * Calculate the total inventory value of all products.
     * <p>
     * Inventory value for one product:
     *     price × stock
     */
    public BigDecimal calculateInventoryValue(
            List<Product> products
    ) {
        return products.stream()
                .map(p ->  p.price().multiply(BigDecimal.valueOf(p.stock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    // ============================================================
    // EASY 4 — Grouping
    // ============================================================

    /**
     * Group products by category.
     * Example:<p>
     * PHONE      -> [...]<p>
     * TABLET     -> [...]<p>
     * LAPTOP     -> [...]<p>
     * HEADPHONES -> [...]
     */
    public Map<Category, List<Product>> groupProductsByCategory(
            List<Product> products
    ) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::category));
    }


    // ============================================================
    // EASY 5 — Grouping + Counting
    // ============================================================

    /**
     * Count how many products belong to each category.<p>
     * Example:<p>
     * PHONE      -> 3<p>
     * TABLET     -> 2<p>
     * LAPTOP     -> 3<p>
     * HEADPHONES -> 4<p>
     */
    public Map<Category, Long> countProductsByCategory(
            List<Product> products
    ) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::category, Collectors.counting()));
    }


    // ============================================================
    // EASY 6 — Map + Aggregation
    // ============================================================

    /**
     * Calculate the total price of one order.<p>
     * For each OrderItem:<p>
     *     product price × quantity<p>
     * Then calculate the total.
     */
    public BigDecimal calculateOrderTotal(
            Order order
    ) {
        return order.items().stream()
                .map(p -> p.product().price().multiply(BigDecimal.valueOf(p.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    // ============================================================
    // EASY 7 — flatMap + Grouping + Summing
    // ============================================================

    /**
     * Calculate the total quantity sold for every product.<p>
     * Ignore CANCELLED orders.<p>
     * Example:<p>
     * iPhone 15      -> 3<p>
     * AirPods Pro    -> 4<p>
     * Galaxy S24     -> 2
     */
    public Map<String, Integer> calculateQuantitySoldByProduct(
            List<Order> orders
    ) {
        return orders.stream()
                .filter(o -> o.status() != OrderStatus.CANCELLED)
                .flatMap(i -> i.items().stream())
                .collect(Collectors.groupingBy(i -> i.product().name(),
                        Collectors.summingInt(OrderItem::quantity)));
    }


    // ============================================================
    // EASY 8 — Customer Spending
    // ============================================================

    /**
     * Calculate how much each customer has spent.<p>
     * Count only:<p>
     * - PAID<p>
     * - SHIPPED<p>
     * - DELIVERED<p>
     * Ignore:<p>
     * - PENDING<p>
     * - CANCELLED
     */
    public Map<Long, BigDecimal> calculateCustomerSpending(
            List<Order> orders
    ) {
        return orders.stream()
                .filter(o -> o.status() != OrderStatus.PENDING && o.status() != OrderStatus.CANCELLED)
                .collect(Collectors.groupingBy(Order::customerId,
                        Collectors.flatMapping(
                                o -> o.items().stream()
                                        .map(i -> i.product().price().multiply(BigDecimal.valueOf(i.quantity()))),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
    }


    // ============================================================
    // ADVANCED 1 — Customer Spending Ranking
    // ============================================================

    /**
     * Calculate total spending for every customer.<p>
     * Count only:<p>
     * - PAID<p>
     * - SHIPPED<p>
     * - DELIVERED<p>
     * Then return the top N customers ranked by spending,<p>
     * from the highest spender to the lowest spender.
     */
    public Map<Long, BigDecimal> findTopCustomersBySpending(
            List<Order> orders,
            int limit
    ) {
        var map = orders.stream()
                .filter(o -> o.status() != OrderStatus.PENDING && o.status() != OrderStatus.CANCELLED)
                .collect(Collectors.groupingBy(Order::customerId,
                        Collectors.flatMapping(
                                o -> o.items().stream()
                                        .map(i -> i.product().price().multiply(BigDecimal.valueOf(i.quantity()))),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        ))
                );
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (value, _) -> value,
                        LinkedHashMap::new
                ));
    }


    // ============================================================
    // ADVANCED 2 — Category Revenue Report
    // ============================================================

    /**
     * Calculate total revenue for each product category.<p>
     * Ignore CANCELLED orders.<p>
     * Then rank categories from the highest revenue to the lowest revenue.
     */
    public Map<Category, BigDecimal> calculateCategoryRevenue(
            List<Order> orders
    ) {
        var quantityByProduct = orders.stream()
                .filter(o -> o.status() != OrderStatus.CANCELLED)
                .flatMap(o -> o.items().stream())
                .collect(Collectors.groupingBy(OrderItem::product, Collectors.summingInt(OrderItem::quantity)));
        var unsortedReport = quantityByProduct.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().category(),
                        entry -> entry.getKey().price().multiply(BigDecimal.valueOf(entry.getValue())),
                        BigDecimal::add,
                        LinkedHashMap::new
                ));
        return unsortedReport.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, _) -> a,
                        LinkedHashMap::new
                ));
    }


    // ============================================================
    // ADVANCED 3 — Customers Who Bought From Multiple Categories
    // ============================================================

    /**
     * Find customers who purchased products from at least<p>
     * 2 different categories.<p>
     * Ignore CANCELLED orders.<p>
     * Return:<p>
     * customerId -> set of categories they purchased from
     */
    public Map<Long, Set<Category>> findMultiCategoryCustomers(
            List<Order> orders
    ) {
        var itemsOfCustomer = orders.stream()
                .filter(o -> o.status() != OrderStatus.CANCELLED)
                .collect(Collectors.groupingBy(Order::customerId,
                        Collectors.flatMapping(
                                o -> o.items().stream()
                                        .map(i -> i.product().category()),
                                Collectors.toSet()
                        )
                ));
        itemsOfCustomer.entrySet().removeIf(it -> it.getValue().size() < 2);
        return itemsOfCustomer;
    }


    // ============================================================
    // ADVANCED 4 — Detect Invalid / Suspicious Orders
    // ============================================================

    /**
     * Find orders containing at least one invalid OrderItem.<p>
     * An item is invalid when:<p>
     * - quantity <= 0<p>
     * OR<p>
     * - quantity > product stock<p>
     * Return all orders containing at least one invalid item.
     */
    public List<Order> findInvalidOrders(
            List<Order> orders
    ) {
        return orders.stream()
                .filter(o -> o.items().stream()
                        .anyMatch(i -> i.quantity() <= 0 || i.quantity() > i.product().stock())
                ).toList();
    }


    // ============================================================
    // ADVANCED 5 — Rule-Based Product Recommendation
    // ============================================================

    /**
     * Recommend products for a customer.<p>
     * Rules:<p>
     * 1. The product must be in stock.<p>
     * 2. The product must belong to a category that
     *    the customer has previously purchased from.<p>
     * 3. The customer must not have purchased that product before.<p>
     * 4. Sort recommendations by price descending.<p>
     * 5. Return at most N products.<p>
     * Ignore CANCELLED orders when examining purchase history.
     */
    public List<Product> recommendProducts(
            Customer customer,
            List<Product> allProducts,
            List<Order> allOrders,
            int limit
    ) {
        Set<Product> purchasedProducts = allOrders.stream()
                .filter(o -> o.customerId() == customer.id() && o.status() != OrderStatus.CANCELLED)
                .flatMap(o -> o.items().stream().map(OrderItem::product))
                .collect(Collectors.toSet());
        return allProducts.stream()
                .filter(p -> !purchasedProducts.contains(p) && p.stock() > 0)
                .sorted(Comparator.comparing(Product::price).reversed())
                .limit(limit)
                .toList();
    }

//*************************************************************
// SECOND TEST
//*************************************************************

// ============================================================
// EASY 1 — Filter + Map + findFirst
// ============================================================

    /**
     * Return the name of the first product that:<p>
     * - belongs to the given category<p>
     * - has a price greater than or equal to minimumPrice<p>
     * Return Optional.empty() when no product matches.
     */
    public Optional<String> findFirstProduct(
            List<Product> products,
            Category category,
            BigDecimal minimumPrice
    ) {
        return products.stream()
                .filter(p -> p.category().equals(category) && p.price().compareTo(minimumPrice) > 0)
                .map(Product::name)
                .findFirst();
    }


// ============================================================
// EASY 2 — Min / Max
// ============================================================

    /**
     * Return the most expensive product.<p>
     * Return Optional.empty() when the product list is empty.
     */
    public Optional<Product> findMostExpensiveProduct(
            List<Product> products
    ) {
        return products.stream()
                .max(Comparator.comparing(Product::price));
    }


    /**
     * Return the cheapest product.<p>
     * Return Optional.empty() when the product list is empty.
     */
    public Optional<Product> findCheapestProduct(
            List<Product> products
    ) {
        return products.stream()
                .min(Comparator.comparing(Product::price));
    }


// ============================================================
// EASY 3 — Partitioning
// ============================================================

    /**
     * Partition products into two groups based on whether they<p>
     * are currently in stock.<p>
     * true  -> products that are in stock<p>
     * false -> products that are out of stock<p>
     * Example:<p>
     * true  -> [iPhone 15, MacBook Air, AirPods Pro]<p>
     * false -> [Galaxy S24]
     */
    public Map<Boolean, List<Product>> partitionByStock(
            List<Product> products
    ) {
        return products.stream()
                .collect(Collectors.partitioningBy(p -> p.stock() > 0));
    }


// ============================================================
// INTERMEDIATE 1 — Match Operations
// ============================================================

    /**
     * Return true when at least one product is out of stock.
     */
    public boolean hasOutOfStockProduct(
            List<Product> products
    ) {
        return products.stream()
                .anyMatch(p -> p.stock() == 0);
    }


    /**
     * Return true when every product has a positive price.
     */
    public boolean allProductsHavePositivePrice(
            List<Product> products
    ) {
        return products.stream()
                .allMatch(p -> p.price().signum() == 1);
    }


    /**
     * Return true when none of the products belong to Category.PHONE.
     */
    public boolean noPhoneProducts(
            List<Product> products
    ) {
        return products.stream()
                .noneMatch(p -> p.category() == Category.PHONE);
    }


// ============================================================
// INTERMEDIATE 2 — toMap + Duplicate Keys
// ============================================================

    /**
     * Create a map containing:<p>
     * product name -> product price<p>
     * Assume that all product names are unique.<p>
     * Example:<p>
     * "iPhone 15"    -> 999.99<p>
     * "Galaxy S24"   -> 799.99<p>
     * "MacBook Air"  -> 1299.99
     */
    public Map<String, BigDecimal> createPriceMap(
            List<Product> products
    ) {
        return products.stream()
                .collect(Collectors.toMap(Product::name, Product::price,
                        (existing, replacement) -> existing));
    }


    /**
     * Create a map containing:<p>
     * product name -> the highest price among products with that name<p>
     * Product names may appear multiple times.<p>
     * Example:<p>
     * "USB Cable" -> 19.99<p>
     * when products contain:<p>
     * USB Cable -> 12.99<p>
     * USB Cable -> 19.99<p>
     * USB Cable -> 15.99
     */
    public Map<String, BigDecimal> createPriceMapKeepingHighestPrice(
            List<Product> products
    ) {
        return products.stream()
                .collect(Collectors.toMap(Product::name, Product::price, BigDecimal::max));
    }


// ============================================================
// INTERMEDIATE 3 — Joining
// ============================================================

    /**
     * Return all product names as one comma-separated string.<p>
     * Example:<p>
     * iPhone 15, Galaxy S24, MacBook Air, AirPods Pro<p>
     * Return an empty string when the list is empty.
     */
    public String joinProductNames(
            List<Product> products
    ) {
        return products.stream()
                .map(Product::name)
                .collect(Collectors.joining(", "));
    }


    /**
     * Return all product names separated by " | ".<p>
     * Example:<p>
     * iPhone 15 | Galaxy S24 | MacBook Air | AirPods Pro
     */
    public String joinProductNamesWithSeparator(
            List<Product> products
    ) {
        return products.stream()
                .map(Product::name)
                .collect(Collectors.joining(" | "));
    }


// ============================================================
// INTERMEDIATE 4 — Multi-Level Grouping
// ============================================================

    /**
     * Group products first by category, then by stock status.<p>
     * Result structure:<p>
     * Category<p>
     *     -> true  -> products in stock<p>
     *     -> false -> products out of stock<p>
     *
     * Example:<p>
     *
     * PHONE<p>
     *     -> true  -> [...]<p>
     *     -> false -> [...]<p>
     *
     * LAPTOP<p>
     *     -> true  -> [...]<p>
     *     -> false -> [...]
     */
    public Map<Category, Map<Boolean, List<Product>>> groupByCategoryAndStock(
            List<Product> products
    ) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::category,
                        Collectors.partitioningBy(p -> p.stock() > 0)));
    }


// ============================================================
// ADVANCED 1 — Order Total with BigDecimal
// ============================================================

    /**
     * Calculate the total value of one order.
     *<p>
     * For each OrderItem:
     *<p>
     *     product price × quantity
     *<p>
     * Then calculate the total value of all items.
     *<p>
     * Return BigDecimal. ZERO when the order contains no items.
     */
    public BigDecimal calculateOrderTotal2(
            Order order
    ) {
        return null;
    }


// ============================================================
// ADVANCED 2 — Customer Spending
// ============================================================

    /**
     * Calculate how much each customer has spent.
     *<p>
     * Count only orders with these statuses:
     * - PAID
     * - SHIPPED
     * - DELIVERED
     *<p>
     * Ignore:
     * - PENDING
     * - CANCELLED
     *<p>
     * For each OrderItem:
     *<p>
     *     product price × quantity
     *<p>
     * Example:
     *<p>
     * 101 -> 1250.00
     * 205 -> 3420.50
     * 317 ->  890.00
     */
    public Map<Long, BigDecimal> calculateCustomerSpending2(
            List<Order> orders
    ) {
        return null;
    }


// ============================================================
// ADVANCED 3 — Top Products by Revenue
// ============================================================

    /**
     * Calculate the total revenue generated by each product.
     *<p>
     * For each OrderItem:
     *<p>
     *     product price × quantity
     *<p>
     * Ignore CANCELLED orders.
     *<p>
     * Then return the top N products ranked by revenue,
     * from the highest revenue to the lowest revenue.
     *<p>
     * The returned map must preserve this ranking order.
     *<p>
     * Example:
     *<p>
     * Laptop      -> 12000.00
     * iPhone 15   ->  9500.00
     * AirPods Pro ->  7300.00
     */
    public LinkedHashMap<Product, BigDecimal> findTopProductsByRevenue2(
            List<Order> orders,
            int limit
    ) {
        return null;
    }


// ============================================================
// ADVANCED 4 — Customers Who Bought From Multiple Categories
// ============================================================

    /**
     * Find customers who purchased products from at least
     * 2 different categories.
     *<p>
     * Ignore CANCELLED orders.
     *<p>
     * Return:
     *<p>
     *     customerId -> set of categories purchased from
     *<p>
     * Example:
     *<p>
     * 101 -> [PHONE, TABLET]
     * 205 -> [LAPTOP, HEADPHONES]
     */
    public Map<Long, Set<Category>> findMultiCategoryCustomers2(
            List<Order> orders
    ) {
        return null;
    }


// ============================================================
// ADVANCED 5 — Detect Invalid Orders
// ============================================================

    /**
     * Find orders containing at least one invalid OrderItem.
     *<p>
     * An OrderItem is invalid when:
     *<p>
     * - quantity <= 0
     * OR
     * - quantity > product stock
     *<p>
     * Return all orders containing at least one invalid item.
     *<p>
     * Example:
     *<p>
     * Order 1001 -> valid
     * Order 1002 -> invalid
     * Order 1003 -> valid
     * Order 1004 -> invalid
     *<p>
     * Result:
     * [Order 1002, Order 1004]
     */
    public List<Order> findInvalidOrders2(
            List<Order> orders
    ) {
        return null;
    }


// ============================================================
// ADVANCED 6 — Rule-Based Product Recommendation
// ============================================================

    /**
     * Recommend products for a customer.
     *<p>
     * Rules:
     *<p>
     * 1. The product must currently be in stock.
     *<p>
     * 2. The product must belong to a category that the customer
     *    has previously purchased from.
     *<p>
     * 3. The customer must not have purchased that product before.
     *<p>
     * 4. Ignore CANCELLED orders when examining purchase history.
     *<p>
     * 5. Sort recommendations by price descending.
     *<p>
     * 6. Return at most N products.
     *<p>
     * Example:
     *<p>
     * Customer previously purchased:
     * - PHONE
     * - TABLET
     *<p>
     * Already owns:
     * - iPhone 15
     *<p>
     * Possible recommendations:
     * - Galaxy S24
     * - iPad Air
     * - Galaxy Tab S9
     *<p>
     * Return at most limit products.
     */
    public List<Product> recommendProducts2(
            Customer customer,
            List<Product> allProducts,
            List<Order> allOrders,
            int limit
    ) {
        return null;
    }


}