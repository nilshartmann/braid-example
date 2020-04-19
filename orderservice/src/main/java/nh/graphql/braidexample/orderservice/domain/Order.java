package nh.graphql.braidexample.orderservice.domain;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Order {

    private String id;
    private LocalDateTime timestamp;
    private String customerName;
    private List<OrderItem> orderItems = new LinkedList<>();

    public Order(String id, LocalDateTime timestamp, String customerName) {
        this.id = id;
        this.timestamp = timestamp;
        this.customerName = customerName;
    }

    public Order addOrderItem(String id, int quantitiy, String productName) {
        orderItems.add(new OrderItem(
            id, quantitiy, productName
        ));

        return this;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id='" + id + '\'' +
            ", timestamp=" + timestamp +
            ", customerName='" + customerName + '\'' +
            ", orderItems=" + orderItems +
            '}';
    }
}
