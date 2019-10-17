package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository;


import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {


}