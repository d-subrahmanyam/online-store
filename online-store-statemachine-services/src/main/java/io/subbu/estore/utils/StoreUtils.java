package io.subbu.estore.utils;

import io.subbu.estore.dtos.OrderLineItemDto;
import io.subbu.estore.dtos.ProductDto;
import io.subbu.estore.models.Product;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class StoreUtils {

    public static Double calculateTotalAmount(Set<OrderLineItemDto> orderLineItemDtos, List<Product> products) {
        AtomicReference<Double> totalAmount = new AtomicReference<>((double) 0);
        orderLineItemDtos.stream()
                .forEach(orderLineItemDto -> {
                    totalAmount.updateAndGet(v -> (double) (v + getProduct(products, orderLineItemDto.getProductDto().getUuid()).getPrice()));
                });
        log.info("Total amount calculated - {}", totalAmount);
        return totalAmount.get();
    }

    public static List<String> getProductUuids(Set<OrderLineItemDto> orderLineItemDtos) {
        List<String> productUuids = new ArrayList<>();
        orderLineItemDtos.stream().forEach(orderLineItemDto -> {
            productUuids.add(orderLineItemDto.getProductDto().getUuid());
        });
        return productUuids;
    }

    private static Product getProduct(List<Product> products, String productUuid) {
        return products.stream().filter(product-> product.getUuid().equals(productUuid)).findFirst().get();
    }
}
