package io.subbu.estore.config;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.subbu.estore.web.interceptors.HeaderRequestInterceptor;
import io.subbu.estore.web.listeners.LocalHttpSessionListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfiguration {

    @Bean
    public Faker randomPerson() {
        return new Faker();
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder().setLenient().create();
    }

    @Bean
    public RestTemplate restTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new HeaderRequestInterceptor("Accept", MediaType.APPLICATION_JSON_VALUE));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    @Bean
    public ServletListenerRegistrationBean<LocalHttpSessionListener> sessionListenerWithMetrics() {
        ServletListenerRegistrationBean<LocalHttpSessionListener> listenerRegBean =
                new ServletListenerRegistrationBean<>();

        listenerRegBean.setListener(new LocalHttpSessionListener());
        return listenerRegBean;
    }

}
