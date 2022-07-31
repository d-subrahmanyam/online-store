package io.subbu.estore.statemachine.actions;

import io.subbu.estore.dtos.OrderDto;
import io.subbu.estore.constants.OrderEvents;
import io.subbu.estore.constants.OrderStates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import static io.subbu.estore.constants.OrderConstants.ORDER_HEADER;

@Slf4j
public class CancelAction implements Action<OrderStates, OrderEvents> {

    @Override
    public void execute(StateContext<OrderStates, OrderEvents> context) {
        OrderDto orderDto = (OrderDto) context.getStateMachine().getExtendedState().getVariables().get(ORDER_HEADER);
        log.info("Cancelled processing order - {}", orderDto);
        orderDto.setStatus(OrderStates.CANCELLED.name());
        context.getStateMachine().getExtendedState().getVariables().put(ORDER_HEADER, orderDto);
    }
}