package io.subbu.estore.services;

import io.subbu.estore.contracts.ServiceI;
import io.subbu.estore.exceptions.ApplicationException;
import io.subbu.estore.exceptions.OrderNotFoundException;
import io.subbu.estore.models.Orders;
import io.subbu.estore.repos.OrdersRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Slf4j
public class OrdersService implements ServiceI<Orders> {

    @Autowired
    private OrdersRepo ordersRepo;

    /**
     * Fetch all the entities
     *
     * @return returns a collection of the entities
     */
    @Override
    public List<Orders> all() {
        return ordersRepo.findAll();
    }

    /**
     * Fetch the entity given its UUID
     *
     * @param uuid UUID of the entity
     * @return returns the entity
     * @throws OrderNotFoundException
     */
    @Override
    public Orders get(String uuid) throws ApplicationException {
        Orders orders = null;
        try {
            orders = ordersRepo.findByUuid(uuid);
        } catch (Exception ex) {
            if (ex instanceof EntityNotFoundException) {
                throw new OrderNotFoundException();
            }
            log.error("Error while processing get order details - \n{}", ExceptionUtils.getStackTrace(ex));
        }
        log.debug("Retrieving the order for [uuid - {}] - {}", uuid, orders);
        return orders;
    }

    /**
     * Save/Update the given entity
     *
     * @param orders given entity
     */
    @Override
    public void save(Orders orders) {
        ordersRepo.save(orders);
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
            ordersRepo.delete(get(uuid));
        } catch (Exception ex) {
            if (ex instanceof ApplicationException) {
                throw new OrderNotFoundException();
            }
        }
    }
}
