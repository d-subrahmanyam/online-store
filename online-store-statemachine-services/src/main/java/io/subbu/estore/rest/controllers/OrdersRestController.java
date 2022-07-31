package io.subbu.estore.rest.controllers;

import io.subbu.estore.dtos.OrderDto;
import io.subbu.estore.services.OrderProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "api/orders")
public class OrdersRestController {

    @Autowired
    private OrderProcessingService orderProcessingService;

    @PostMapping(value = "/createOrder", consumes = "application/json", produces = "application/json")
    public void createOrder(@RequestBody OrderDto orderDto) {
        log.info("Creating the order with details - {}", orderDto);
        orderProcessingService.createOrder(orderDto);
    }

    @PostMapping(value = "/makePayment", consumes = "application/json", produces = "application/json")
    public void makePayment(@RequestBody OrderDto orderDto) {
        log.info("Updating the payment for the order with details - {}", orderDto);
        orderProcessingService.makePayment(orderDto);
    }

    @GetMapping(value = "/orderStatus/{orderUuid}", produces = "application/json")
    public OrderDto orderStatus(@PathVariable String orderUuid) {
        OrderDto orderDto = OrderDto.builder().uuid(orderUuid).build();
        log.info("Retrieving the order with details - {}", orderDto);
        return orderProcessingService.orderStatus(orderDto);
    }
}
