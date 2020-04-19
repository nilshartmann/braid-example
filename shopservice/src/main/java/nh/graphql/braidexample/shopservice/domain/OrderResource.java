package nh.graphql.braidexample.shopservice.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class OrderResource {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/api/orders")
    public List<Order> orders() {
        return orderRepository.findAll();
    }

    @GetMapping("/api/orders/{orderId}")
    public Order order(@PathVariable String orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format(
                        "Order with id '%s' not found", orderId
                    ))
            );
    }

}
