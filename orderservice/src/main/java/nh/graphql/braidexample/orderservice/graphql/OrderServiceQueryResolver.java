package nh.graphql.braidexample.orderservice.graphql;

import graphql.kickstart.tools.GraphQLQueryResolver;
import nh.graphql.braidexample.orderservice.domain.Order;
import nh.graphql.braidexample.orderservice.domain.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceQueryResolver implements GraphQLQueryResolver {
    private static final Logger log = LoggerFactory.getLogger( OrderServiceQueryResolver.class );

    @Autowired
    private OrderRepository orderRepository;

    public Order getOrderById(String orderId) {
        log.info("### getOrderById '{}'", orderId);

        return orderRepository.findById(orderId).orElse(null);
    }

    public List<Order> getOrders() {
        log.info("### getOrders()");
        return orderRepository.findAll();
    }

}
