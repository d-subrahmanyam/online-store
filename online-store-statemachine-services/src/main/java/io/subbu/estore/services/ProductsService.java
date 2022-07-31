package io.subbu.estore.services;

import io.subbu.estore.contracts.ServiceI;
import io.subbu.estore.exceptions.ApplicationException;
import io.subbu.estore.models.Product;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class ProductsService implements ServiceI<Product> {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.service.url.product}")
    private String productServiceRestUrl;

    /**
     * Fetch all the entities
     *
     * @return returns a collection of the entities
     */
    @Override
    @SneakyThrows
    public List<Product> all() {
        ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(productServiceRestUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {});
        return responseEntity.getBody();
    }

    /**
     * Fetch the entity given its UUID
     *
     * @param uuid UUID of the entity
     * @return returns the entity
     * @throws ApplicationException
     */
    @Override
    public Product get(String uuid) throws ApplicationException {
        ResponseEntity<Product> responseEntity = restTemplate.exchange(productServiceRestUrl,HttpMethod.GET, null, Product.class, uuid);
        return responseEntity.getBody();
    }

    public List<Product> selected(List<String> productUuids) {
        ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(productServiceRestUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {}, productUuids);
        return responseEntity.getBody();
    }

    /**
     * Save/Update the given entity
     *
     * @param product given entity
     */
    @Override
    public void save(Product product) {
        throw new UnsupportedOperationException();
    }

    /**
     * Delete the entity for the given UUID
     *
     * @param uuid UUID of the entity
     * @throws ApplicationException
     */
    @Override
    public void delete(String uuid) throws ApplicationException {
        throw new UnsupportedOperationException();
    }
}
