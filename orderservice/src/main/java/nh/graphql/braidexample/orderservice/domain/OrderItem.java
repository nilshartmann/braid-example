package nh.graphql.braidexample.orderservice.domain;

public class OrderItem {
    private String id;
    private int quanitity;
    private String productName;

    public OrderItem(String id, int quanitity, String productName) {
        this.id = id;
        this.quanitity = quanitity;
        this.productName = productName;
    }

    public String getId() {
        return id;
    }

    public int getQuanitity() {
        return quanitity;
    }

    public String getProductName() {
        return productName;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
            "id='" + id + '\'' +
            ", quanitity=" + quanitity +
            ", productName='" + productName + '\'' +
            '}';
    }
}
