package nh.graphql.braidexample.deliveryservice.domain;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class DeliveryRepository {

    private final List<Delivery> deliveries = new LinkedList<>();

    public Optional<Delivery> findById(String orderId) {
        return deliveries.stream()
            .filter(order -> orderId.equals(order.getId()))
            .findFirst();
    }

    public List<Delivery> findAll() {
        return Collections.unmodifiableList(deliveries);
    }

    @PostConstruct
    public void initOrders() {

        Customer[] customers = {
            new Customer("delivery-customer-1", "Klaus Dieter", "Highway 1234"),
            new Customer("delivery-customer-2", "Susi Müller", "Mainstreet 57a")
        };

        deliveries.add(
            new Delivery(
                "delivery-1",
                customers[0],
                "order-1",
                alsZeit("2020-03-17 13:02")
            )
        );

        deliveries.add(
            new Delivery(
                "delivery-2",
                customers[1],
                "order-2",
                alsZeit("2020-03-19 17:12")
            )
        );
    }

    private static LocalDateTime alsZeit(String zeitstring) {
        return LocalDateTime.parse(zeitstring, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }


}
