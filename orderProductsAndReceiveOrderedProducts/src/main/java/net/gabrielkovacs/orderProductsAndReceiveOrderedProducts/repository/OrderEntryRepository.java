package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository;


import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.OrderEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEntryRepository extends JpaRepository<OrderEntry, Long> {


}