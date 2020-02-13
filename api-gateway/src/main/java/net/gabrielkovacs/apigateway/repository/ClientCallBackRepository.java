package net.gabrielkovacs.apigateway.repository;

import net.gabrielkovacs.apigateway.entities.ClientCallBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientCallBackRepository extends JpaRepository<ClientCallBack,String> {
}
