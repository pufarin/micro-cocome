package net.gabrielkovacs.apigateway.repository;

import net.gabrielkovacs.apigateway.entities.ClientCallBack;
import net.gabrielkovacs.apigateway.entities.DeliveryReportsProcessingState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryReportsRepository extends JpaRepository<DeliveryReportsProcessingState,String> {
}
