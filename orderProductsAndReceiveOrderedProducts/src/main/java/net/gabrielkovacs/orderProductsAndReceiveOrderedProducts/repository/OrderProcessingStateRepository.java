package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository;

import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.OrderProcessingState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProcessingStateRepository extends JpaRepository<OrderProcessingState, String> {

}