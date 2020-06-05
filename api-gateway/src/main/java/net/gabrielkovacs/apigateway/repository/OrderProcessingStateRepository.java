package net.gabrielkovacs.apigateway.repository;


import net.gabrielkovacs.apigateway.entities.OrderProcessingState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProcessingStateRepository extends JpaRepository<OrderProcessingState, String> {

}
