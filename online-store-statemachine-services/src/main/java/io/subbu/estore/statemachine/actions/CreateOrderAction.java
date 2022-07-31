package io.subbu.estore.statemachine.actions;

import com.google.gson.Gson;
import io.subbu.estore.dtos.OrderDto;
import io.subbu.estore.models.Order;
import io.subbu.estore.models.Orders;
import io.subbu.estore.services.OrdersService;
import io.subbu.estore.constants.OrderEvents;
import io.subbu.estore.constants.OrderStates;
import io.subbu.estore.statemachine.exceptions.OrderIdNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import static io.subbu.estore.constants.OrderConstants.ORDER_HEADER;

@Slf4j
public class CreateOrderAction implements Action<OrderStates, OrderEvents> {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private Gson gson;

    @SneakyThrows
    @Override
    public void execute(StateContext<OrderStates, OrderEvents> context) {
        OrderDto orderDto = context.getMessage().getHeaders().get(ORDER_HEADER, OrderDto.class);
        log.info("Created order with [order details = {}]", orderDto);
        if (orderDto == null) throw new OrderIdNotFoundException();
        orderDto.setStatus(OrderStates.CREATE.name());
        saveOrder(orderDto.createEntity());
        context.getStateMachine().getExtendedState().getVariables().put(ORDER_HEADER, orderDto);
    }

    private void saveOrder(Order order) {
        Orders orders = Orders.builder().uuid(order.getUuid()).orderJson(gson.toJson(order)).build();
        log.debug("Saving the order to DB - {}", orders);
        ordersService.save(orders);
    }
}