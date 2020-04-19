package nh.graphql.braidexample.deliveryservice.domain;

public class Customer {
    private String id;
    private String name;
    private String shippingAddress;

    public Customer(String id, String name, String shippingAddress) {
        this.id = id;
        this.name = name;
        this.shippingAddress = shippingAddress;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", shippingAddress='" + shippingAddress + '\'' +
            '}';
    }
}
