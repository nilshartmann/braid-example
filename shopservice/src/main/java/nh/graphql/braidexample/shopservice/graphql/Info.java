package nh.graphql.braidexample.shopservice.graphql;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Info {
    @Value("${braid-example.enable-braid}")
    private boolean braidEnabled;

    @Value("${braid-example.enable-async}")
    private boolean asyncEnabled;

    public boolean isBraid() {
        return braidEnabled;
    }

    public boolean isAsync() {
        return asyncEnabled;
    }

}
