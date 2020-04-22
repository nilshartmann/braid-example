package nh.graphql.braidexample.shopservice.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private Info info;

    public Info getInfo() {
        return this.info;
    }
}
