package io.subbu.estore.contracts;

import io.subbu.estore.dtos.OrderDto;

public interface OrderProcessingServiceI {

    /**
     * This method submits the order details
     * to the state machine for processing
     * @param orderDto
     * @return updated orderDto
     */
    OrderDto createOrder(OrderDto orderDto);

    /**
     * This method sumits the order payment details
     * to the state machine for processing
     * @param orderDto
     * @return updated orderDto
     */
    OrderDto makePayment(OrderDto orderDto);

    /**
     * This method retrieves the order status details
     * for a given order uuid
     * @param orderDto
     * @return orderDto
     */
    OrderDto orderStatus(OrderDto orderDto);
}
