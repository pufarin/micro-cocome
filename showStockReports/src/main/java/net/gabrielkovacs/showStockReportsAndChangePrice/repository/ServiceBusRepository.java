package net.gabrielkovacs.showStockReportsAndChangePrice.repository;

import net.gabrielkovacs.showStockReportsAndChangePrice.entities.ServiceBusMessageCommand;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceBusRepository extends JpaRepository<ServiceBusMessageCommand, String> {
}
