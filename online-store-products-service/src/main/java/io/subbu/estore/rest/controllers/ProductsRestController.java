package io.subbu.estore.rest.controllers;

import io.subbu.estore.contracts.ControllerI;
import io.subbu.estore.models.Product;
import io.subbu.estore.services.ProductService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "api/products")
public class ProductsRestController implements ControllerI<Product> {

    @Autowired
    private ProductService productService;

    /**
     * This method retrieves all the
     * entities from the repo for the
     * given type T
     *
     * @return List<Product>
     */
    @Override
    @GetMapping(produces = "application/json")
    public List<Product> all() {
        return productService.all();
    }

    /**
     * This method retrieves a single
     * entity of type T for the given
     * uuid from the repo
     *
     * @param productUuid
     * @return Product
     */
    @Override
    @GetMapping(value = "/{productUuid}", produces = "application/json")
    @SneakyThrows
    public Product get(@PathVariable String productUuid) {
        return productService.get(productUuid);
    }

    /**
     * This method saves the given entity
     * of type T to the repo
     *
     * @param product
     */
    @Override
    @PostMapping
    public void save(Product product) {
        productService.save(product);
    }

    /**
     * This method deletes the entity
     * of type T from the repo for the
     * given uuid
     *
     * @param productUuid
     */
    @Override
    @DeleteMapping(value = "/{productUuid}")
    @SneakyThrows
    public void delete(@PathVariable String productUuid) {
        productService.delete(productUuid);
    }

    @PostMapping(value = "/selected")
    public List<Product> selected(@RequestBody List<String> productUuids) {
        return productService.selected(productUuids);
    }
}
