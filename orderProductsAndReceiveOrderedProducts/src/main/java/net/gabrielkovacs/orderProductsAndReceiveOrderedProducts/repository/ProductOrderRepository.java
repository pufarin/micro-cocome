package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository;


import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

    @Query(value = "select * from productorder po where po.id = (select product_order_id from orderentry o where o.id=?1)", nativeQuery = true)
    Optional<ProductOrder> getProductOrderByOrderEntryId(long orderEntryId);

}