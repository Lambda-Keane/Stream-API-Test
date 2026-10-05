void main() {
    Customer ALICE = new Customer(1, "Alice");
    final List<Product> products = SampleData.PRODUCTS;
    final List<Order> orders = SampleData.ORDERS;
    ProductAnalytics debug = new ProductAnalytics();
    System.out.println(debug.recommendProducts(ALICE, products, orders, 5));
}
