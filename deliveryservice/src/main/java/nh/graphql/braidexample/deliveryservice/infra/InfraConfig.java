package nh.graphql.braidexample.deliveryservice.infra;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfraConfig {

    @Bean
    public FilterRegistrationBean<AuthenticationFilter>
        authenticationFilterFilterRegistration(){
        FilterRegistrationBean<AuthenticationFilter> registrationBean
            = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.addUrlPatterns("/graphql/*");

        return registrationBean;
    }

}
