package nh.graphql.braidexample.deliveryservice.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import nh.graphql.braidexample.deliveryservice.domain.Delivery;
import nh.graphql.braidexample.deliveryservice.domain.DeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryServiceQueryResolver implements GraphQLQueryResolver {
    private static final Logger log = LoggerFactory.getLogger( DeliveryServiceQueryResolver.class );

    @Autowired
    private DeliveryRepository deliveryRepository;

    public Delivery delivery(String deliveryId) {
        log.info("### delivery '{}'", deliveryId);

        return deliveryRepository.findById(deliveryId).orElse(null);
    }

    public List<Delivery> deliveries() {
        log.info("### deliveries()");
        return deliveryRepository.findAll();
    }

}
