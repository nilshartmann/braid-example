package nh.graphql.braidexample.deliveryservice.domain;

import java.time.LocalDateTime;

public class Delivery {
    private String id;
    private Customer customer;
    private String orderId;
    private LocalDateTime shippingTime;

    public Delivery(String id, Customer customer, String orderId, LocalDateTime shippingTime) {
        this.id = id;
        this.customer = customer;
        this.orderId = orderId;
        this.shippingTime = shippingTime;
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getOrderId() {
        return orderId;
    }

    public LocalDateTime getShippingTime() {
        return shippingTime;
    }
}
