package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository;

import java.util.ArrayList;
import java.util.List;

import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ProductOrder;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ProductDeliveryDuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

    @Query(value = "select * from productorder po where po.id = (select product_order_id from orderentry o where o.id=?1)", nativeQuery = true)
    Optional<ProductOrder> getProductOrderByOrderEntryId(long orderEntryId);

    @Query(value = "select oe.product_id as productId , (po.delivery_date - po.ordering_date) as nrDays from productorder po join orderentry oe " +
                    "on oe.product_order_id = po.id where oe.product_id in (?1)", nativeQuery = true)
    List<ProductDeliveryDuration> getNrDaysPerProductDelivery(List<Long> productsId);

}