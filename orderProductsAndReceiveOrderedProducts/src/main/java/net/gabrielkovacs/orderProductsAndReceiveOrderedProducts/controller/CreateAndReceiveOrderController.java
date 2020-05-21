package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.controller;

import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.OrderEntry;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ReceivedOrder;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.flow.OrderProcessingSubscriber;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services.OrderProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;


@RestController
public class CreateAndReceiveOrderController {

    Logger log = LoggerFactory.getLogger(CreateAndReceiveOrderController.class);

    private OrderProductService orderProductService;
    private OrderProcessingSubscriber orderProcessingSubscriber;

    public CreateAndReceiveOrderController(OrderProductService orderProductService, OrderProcessingSubscriber orderProcessingSubscriber){
        this.orderProductService = orderProductService;
        this.orderProcessingSubscriber = orderProcessingSubscriber;
    }

    @Operation(summary = "UC3 order products")
    @PostMapping("store/{storeId}/order")
    public ResponseEntity<OrderEntry> orderProduct(@RequestBody OrderEntry orderEntry, @PathVariable Long storeId){
        OrderEntry toReturn =  orderProductService.orderProduct(orderEntry,storeId);

        return new ResponseEntity<OrderEntry>(toReturn, HttpStatus.CREATED);
    }

    @Operation(summary = "UC4 receive ordered products", description = "Adds the delivery date into the ProductOrder table as well as updates the stock (needs showStockReports) ")
    @PutMapping("product-order/{orderId}")
    ResponseEntity<?> receivedOrder(@RequestBody ReceivedOrder receivedOrder,  @PathVariable Long orderId) throws InterruptedException {
        String correlationId = orderProductService.generateCorrelationId();
        orderProductService.receiveOrder(orderId, correlationId, receivedOrder);

        while (!correlationId.equals(orderProcessingSubscriber.getResponse().getUuid())){
            log.info("The correlationId: {}, theOtherOne {}", correlationId, orderProcessingSubscriber.getResponse().getUuid());
            Thread.sleep(5);
        }
        log.info("Order Processing: {}",orderProcessingSubscriber.getResponse());
        if(orderProcessingSubscriber.getResponse().getPayload().equals("NOT FOUND")){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.OK);
        }

    }

}