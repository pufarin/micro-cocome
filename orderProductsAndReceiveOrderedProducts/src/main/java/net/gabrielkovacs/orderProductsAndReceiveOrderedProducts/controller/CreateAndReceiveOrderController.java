package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.controller;

import io.swagger.v3.oas.annotations.Operation;
import net.gabrielkovacs.common.entities.OrderEntry;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ReceivedOrder;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services.OrderProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class CreateAndReceiveOrderController {

    private OrderProductService orderProductService;

    public CreateAndReceiveOrderController(OrderProductService orderProductService){
        this.orderProductService = orderProductService;
    }

    @Operation(summary = "UC3 order products")
    @PostMapping("store/{storeId}/order")
    public ResponseEntity<OrderEntry> orderProduct(@RequestBody OrderEntry orderEntry, @PathVariable Long storeId){
        OrderEntry toReturn =  orderProductService.orderProduct(orderEntry,storeId);
        return new ResponseEntity<OrderEntry>(toReturn, HttpStatus.CREATED);
    }

    @Operation(summary = "UC4 receive ordered products", description = "Adds the delivery date into the ProductOrder table as well as updates the stock")
    @PutMapping("product-order/{orderId}")
    ResponseEntity<?> receivedOrder(@RequestBody ReceivedOrder receivedOrder, @PathVariable Long orderId){
        return orderProductService.updateProductOrderDeliveryDate(receivedOrder,orderId);
    }

}
