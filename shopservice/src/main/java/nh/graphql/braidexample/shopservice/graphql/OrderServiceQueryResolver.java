package nh.graphql.braidexample.shopservice.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import nh.graphql.braidexample.shopservice.domain.Order;
import nh.graphql.braidexample.shopservice.domain.OrderRepository;
import org.dataloader.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@ConditionalOnProperty(value = "braid-example.enable-async", havingValue = "false", matchIfMissing = true)
@Service
public class OrderServiceQueryResolver implements GraphQLQueryResolver {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceQueryResolver.class);

    public OrderServiceQueryResolver() {
        log.info("Using synchronous QueryResolver");
    }

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
