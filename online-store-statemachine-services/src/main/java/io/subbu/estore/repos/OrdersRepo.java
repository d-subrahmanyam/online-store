package io.subbu.estore.repos;

import io.subbu.estore.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepo extends JpaRepository<Orders, Long> {

    Orders findByUuid(String uuid);
}
