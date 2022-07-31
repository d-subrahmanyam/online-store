package io.subbu.estore.services;

import io.subbu.estore.contracts.ServiceI;
import io.subbu.estore.exceptions.ApplicationException;
import io.subbu.estore.exceptions.ProductNotFoundException;
import io.subbu.estore.models.Product;
import io.subbu.estore.repos.ProductRepo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ProductService implements ServiceI<Product> {

    @Autowired
    private ProductRepo productRepo;

    /**
     * Fetch all the entities
     *
     * @return returns a collection of the entities
     */
    @Override
    public List<Product> all() {
        return productRepo.findAll();
    }

    /**
     * Fetch the entity given its UUID
     *
     * @param uuid UUID of the entity
     * @return returns the entity
     * @throws ProductNotFoundException
     */
    @Override
    public Product get(String uuid) throws ApplicationException {
        Product product = null;
        try {
            product = productRepo.findByUuid(uuid);
        } catch (Exception ex) {
            if (ex instanceof EntityNotFoundException) {
                throw new ProductNotFoundException();
            }
        }
        return product;
    }

    /**
     * Save/Update the given entity
     *
     * @param product given entity
     */
    @Override
    public void save(Product product) {
        productRepo.save(product);
    }

    /**
     * Delete the entity for the given UUID
     *
     * @param uuid UUID of the entity
     * @throws ApplicationException
     */
    @Override
    public void delete(String uuid) throws ApplicationException {
        try {
            productRepo.delete(get(uuid));
        } catch (Exception ex) {
            if (ex instanceof ProductNotFoundException) {
                throw new ProductNotFoundException();
            }
        }
    }

    @SneakyThrows
    public List<Product> selected(List<String> productUuids) {
        return productRepo.findAllByUuidIn(productUuids);
    }
}
