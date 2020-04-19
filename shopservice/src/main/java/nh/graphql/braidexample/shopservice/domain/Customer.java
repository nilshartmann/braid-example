package nh.graphql.braidexample.shopservice.domain;

public class Customer {
    private String id;
    private String name;
    private String state;

    public Customer(String id, String name, String state) {
        this.id = id;
        this.name = name;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", state='" + state + '\'' +
            '}';
    }
}
