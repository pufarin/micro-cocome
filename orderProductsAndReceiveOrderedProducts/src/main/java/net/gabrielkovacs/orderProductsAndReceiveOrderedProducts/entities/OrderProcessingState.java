package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProcessingState {

    @Id
    private String requestUUID;
    private String orderState;
    private String eventName;
    private Integer amount;
    private Long productId;
    private Long storeId;
}
