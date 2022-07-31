package io.subbu.estore.statemachine.actions;

import com.google.gson.Gson;
import io.subbu.estore.dtos.OrderDto;
import io.subbu.estore.exceptions.ApplicationException;
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
import static io.subbu.estore.constants.OrderConstants.PAYMENT_HEADER;

@Slf4j
public class PaymentAction implements Action<OrderStates, OrderEvents> {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private Gson gson;

    @SneakyThrows
    @Override
    public void execute(StateContext<OrderStates, OrderEvents> context) {
        OrderDto orderDto = context.getMessage().getHeaders().get(PAYMENT_HEADER, OrderDto.class);
        log.info("Paid [amount = {}}] for order with [order details = {}]", orderDto.getAmountPaid(), orderDto.getUuid());
        if (orderDto == null) throw new OrderIdNotFoundException();
        Orders _orders = ordersService.get(orderDto.getUuid());
        if (validateAmountPaid(orderDto, _orders)) {
            orderDto = updateAmountPaid(Double.valueOf(orderDto.getAmountPaid()), _orders);
            context.getStateMachine().getExtendedState().getVariables().put(ORDER_HEADER, orderDto);
        }
    }

    @SneakyThrows
    private OrderDto updateAmountPaid(Double amountPaid, Orders orders) {
        Order _order = gson.fromJson(orders.getOrderJson(), Order.class);
        _order.setAmountPaid(amountPaid);
        _order.setStatus(OrderStates.PAID.name());
        orders.setOrderJson(gson.toJson(_order));
        ordersService.save(orders);
        return OrderDto.builder().build().createDtoFromEntity(_order);
    }

    @SneakyThrows
    private boolean validateAmountPaid(OrderDto orderDto, Orders orders) {
        boolean isTotalAmountPaid = false;
        Order _order = gson.fromJson(orders.getOrderJson(), Order.class);
        if(orderDto.getAmountPaid().equals(_order.getTotalAmount())) {
            isTotalAmountPaid = true;
        }
        log.info("[isTotalAmountPaid = {}] | [Order uuid = {}]", isTotalAmountPaid, orderDto.getUuid());
        return isTotalAmountPaid;
    }

}