package net.gabrielkovacs.common.repository;

import java.util.List;

import net.gabrielkovacs.common.entities.ProductSuplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface ProductSuplierRepository extends JpaRepository<ProductSuplier, Long>{

    @Query(value = "select ps.id from productsuplier ps, tradingenterprise te, tradinge_products  te_ps where te.id = te_ps.trading_enterprise_id and" +
                    " ps.id = te_ps.product_supplier_id and te.id = ?1", nativeQuery = true)
    List<Long> getAllProductSupliersIdsPerEnterprise(long enterpriseId);
    /*
    @Query(value = "select p.id from product p where p.productsuplierid in (?1)", nativeQuery = true)
    List<Long> getAllProductIdsPerProductSuplier(List<Long> productSupplierIds);
        */
    @Query(value = "select p.id from product p where p.productsuplierid = ?1", nativeQuery = true)
    List<Long> getAllProductIdsPerProductSuplier(long productSupplierId);
}