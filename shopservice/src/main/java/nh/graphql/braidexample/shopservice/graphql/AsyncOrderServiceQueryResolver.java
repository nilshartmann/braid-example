package nh.graphql.braidexample.shopservice.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import nh.graphql.braidexample.shopservice.domain.Order;
import nh.graphql.braidexample.shopservice.domain.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

@Service
@ConditionalOnProperty(value = "braid-example.enable-async", havingValue = "true")
public class AsyncOrderServiceQueryResolver implements GraphQLQueryResolver {
    private static final Logger log = LoggerFactory.getLogger(AsyncOrderServiceQueryResolver.class);

    public AsyncOrderServiceQueryResolver() {
        log.info("Using synchronous AsyncOrderServiceQueryResolver");
    }

    @Autowired
    private OrderRepository orderRepository;

    public CompletableFuture<Order> getOrderById(String orderId) {
        return CompletableFuture.supplyAsync(
            () -> {
                sleep(2);
                return orderRepository.findById(orderId).orElse(null);
            }
        );

    }

    public CompletableFuture<List<Order>> getOrders() {
        return CompletableFuture.supplyAsync(
            () -> {
                sleep(2);
                return orderRepository.findAll();
            }
        );
    }

    private void sleep(long delayInSeconds) {
        try {
            Thread.sleep(delayInSeconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
