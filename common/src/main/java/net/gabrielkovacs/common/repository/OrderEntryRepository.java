package net.gabrielkovacs.common.repository;


import net.gabrielkovacs.common.entities.OrderEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEntryRepository extends JpaRepository<OrderEntry, Long> {

    @Query(value = "select * from orderentry where id = ?1", nativeQuery = true)
    OrderEntry getProductId(long orderEntryId);

}