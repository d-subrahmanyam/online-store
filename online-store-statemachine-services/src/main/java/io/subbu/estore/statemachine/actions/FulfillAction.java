package io.subbu.estore.statemachine.actions;

import com.google.gson.Gson;
import io.subbu.estore.dtos.OrderDto;
import io.subbu.estore.exceptions.ApplicationException;
import io.subbu.estore.models.Order;
import io.subbu.estore.models.Orders;
import io.subbu.estore.services.OrdersService;
import io.subbu.estore.constants.OrderEvents;
import io.subbu.estore.constants.OrderStates;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import static io.subbu.estore.constants.OrderConstants.ORDER_HEADER;

@Slf4j
public class FulfillAction implements Action<OrderStates, OrderEvents> {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private Gson gson;

    @Override
    public void execute(StateContext<OrderStates, OrderEvents> context) {
        OrderDto orderDto = (OrderDto) context.getStateMachine().getExtendedState().getVariables().get(ORDER_HEADER);
        orderDto = saveOrder(orderDto.getUuid());
        log.info("Fulfilled processing order  - {}", orderDto);
        context.getStateMachine().getExtendedState().getVariables().put(ORDER_HEADER, orderDto);
    }

    @SneakyThrows(ApplicationException.class)
    private OrderDto saveOrder(String uuid) {
        Orders _orders = ordersService.get(uuid);
        Order _order  = gson.fromJson(_orders.getOrderJson(), Order.class);
        if (_order.getTotalAmount().equals(_order.getAmountPaid())) {
            _order.setStatus(OrderStates.FULFILLED.name());
        }
        _orders.setOrderJson(gson.toJson(_order));
        ordersService.save(_orders);
        return OrderDto.builder().build().createDtoFromEntity(_order);
    }
}