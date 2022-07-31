package io.subbu.estore.services;

import com.google.gson.Gson;
import io.subbu.estore.constants.OrderEvents;
import io.subbu.estore.constants.OrderStates;
import io.subbu.estore.contracts.OrderProcessingServiceI;
import io.subbu.estore.dtos.OrderDto;
import io.subbu.estore.models.Order;
import io.subbu.estore.models.Orders;
import io.subbu.estore.models.Product;
import io.subbu.estore.utils.StoreUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.subbu.estore.constants.OrderConstants.ORDER_HEADER;
import static io.subbu.estore.constants.OrderConstants.PAYMENT_HEADER;

@Service
@Slf4j
public class OrderProcessingService implements OrderProcessingServiceI {

    @Autowired
    private StateMachine<OrderStates, OrderEvents> stateMachine;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private Gson gson;

    /**
     * This method submits the order details
     * to the state machine for processing
     *
     * @param orderDto
     * @return updated orderDto
     */
    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        log.info("Started processing createOrder for order with uuid - {}", orderDto.getUuid());
        //stateMachine.start();
        if(checkIfOrderAlreadyExists(orderDto)) {
            log.error("Looks like an order with this [uuid={}] already exists", orderDto.getUuid());
            stateMachine.sendEvent(OrderEvents.CANCEL);
            return null;
        }
        List<Product> products = productsService.selected(StoreUtils.getProductUuids(orderDto.getOrderLineItemDtos()));
        orderDto.setTotalAmount(StoreUtils.calculateTotalAmount(orderDto.getOrderLineItemDtos(), products));
        Message<OrderEvents> placeOrderMessage =
                MessageBuilder.withPayload(OrderEvents.CREATE_ORDER)
                        .setHeader(ORDER_HEADER, orderDto)
                        .build();
        stateMachine.sendEvent(placeOrderMessage);
        OrderDto _orderDto = (OrderDto) stateMachine.getExtendedState().getVariables().get(ORDER_HEADER);
        log.info("Finished processing createOrder for with uuid - {} \nUpdated Order Details - {}", orderDto.getUuid(), _orderDto);
        return _orderDto;
    }

    /**
     * This method sumits the order payment details
     * to the state machine for processing
     *
     * @param orderDto
     * @return updated orderDto
     */
    @Override
    public OrderDto makePayment(OrderDto orderDto) {
        log.info("Started processing createOrder for order with uuid - {}", orderDto.getUuid());
        Message<OrderEvents> makePaymentMessage =
                MessageBuilder.withPayload(OrderEvents.PAY)
                        .setHeader(PAYMENT_HEADER, orderDto)
                        .build();
        stateMachine.sendEvent(makePaymentMessage);
        OrderDto _orderDto = (OrderDto) stateMachine.getExtendedState().getVariables().get(ORDER_HEADER);
        log.info("Finished processing makePayment for with uuid - {} Updated Order Details - {}", orderDto.getUuid(), _orderDto);
        if(_orderDto.getStatus().equals(OrderStates.PAID.name())) {
            stateMachine.sendEvent(OrderEvents.FULFILL);
        }
        return _orderDto;
    }

    /**
     * This method retrieves the order status details
     * for a given order uuid
     *
     * @param orderDto
     * @return orderDto
     */
    @Override
    @SneakyThrows
    public OrderDto orderStatus(OrderDto orderDto) {
        log.info("Started processing orderStatus for order with uuid - {}", orderDto.getUuid());
        Orders orders = ordersService.get(orderDto.getUuid());
        log.info("Order details from DB - {}", orders);
        Order order = gson.fromJson(orders.getOrderJson(), Order.class);
        OrderDto _orderDto = OrderDto.builder().build().createDtoFromEntity(order);
        log.info("Finished processing orderStatus for with uuid - {} ", _orderDto);
        return _orderDto;
    }

    @SneakyThrows
    private boolean checkIfOrderAlreadyExists(OrderDto orderDto) {
        boolean orderAlreadyExists = false;
        Orders orders = ordersService.get(orderDto.getUuid());
        if(orders != null) {
            orderAlreadyExists = true;
        }
        return orderAlreadyExists;
    }
}
