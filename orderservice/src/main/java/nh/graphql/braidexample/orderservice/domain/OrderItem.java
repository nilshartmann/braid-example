package nh.graphql.braidexample.orderservice.domain;

public class OrderItem {
    private String id;
    private int quantity;
    private String productName;

    public OrderItem(String id, int quantity, String productName) {
        this.id = id;
        this.quantity = quantity;
        this.productName = productName;
    }

    public String getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
            "id='" + id + '\'' +
            ", quanitity=" + quantity +
            ", productName='" + productName + '\'' +
            '}';
    }
}
