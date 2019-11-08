package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.controller;

import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.OrderEntry;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ProductDeliveryDuration;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ReceivedOrder;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.StockItem;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services.OrderProductService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class CreateAndReceiveOrderController {

    private OrderProductService orderProductService;
    private final String baseUri = "http://localhost:8085";

    public CreateAndReceiveOrderController(OrderProductService orderProductService){
        this.orderProductService = orderProductService;
    }


    @PostMapping("store/{storeId}/order")
    public OrderEntry orderProduct(@RequestBody OrderEntry orderEntry, @PathVariable Long storeId){
        return orderProductService.orderProduct(orderEntry,storeId);
    }

    @PutMapping("product-order/{orderId}")
    ResponseEntity<?> receivedOrder(@RequestBody ReceivedOrder receivedOrder, @PathVariable Long orderId){
        return orderProductService.updateProductOrderDeliveryDate(receivedOrder,orderId);
    }

    @GetMapping("product-order/delivery-time")
    ResponseEntity<List<ProductDeliveryDuration>> getDeliveryDurationPerProduct(@RequestParam ArrayList<Long> productsId){
        return orderProductService.getDeliveryDurationPerProduct(productsId);
    }

    @GetMapping("stockitem")
    public ResponseEntity<StockItem> getStuff() {
        this.orderProductService.setWebClientBaseUri(this.baseUri);
        return orderProductService.getStockItem(200, 100);
    }
}
