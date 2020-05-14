package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.controller;

import net.gabrielkovacs.common.entities.OrderEntry;
import net.gabrielkovacs.common.models.ProductDeliveryDuration;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ReceivedOrder;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.StockItem;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services.OrderProductService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;


@RestController
public class CreateAndReceiveOrderController {

    private OrderProductService orderProductService;
    private final String baseUri = "http://localhost:8085";

    public CreateAndReceiveOrderController(OrderProductService orderProductService){
        this.orderProductService = orderProductService;
    }

    @Operation(summary = "UC3 order products")
    @PostMapping("store/{storeId}/order")
    public OrderEntry orderProduct(@RequestBody OrderEntry orderEntry, @PathVariable Long storeId){
        return orderProductService.orderProduct(orderEntry,storeId);
    }

    @Operation(summary = "UC4 receive ordered products", description = "Adds the delivery date into the ProductOrder table as well as updates the stock (needs showStockReports) ")
    @PutMapping("product-order/{orderId}")
    ResponseEntity<?> receivedOrder(@RequestBody ReceivedOrder receivedOrder, @PathVariable Long orderId){
        return orderProductService.updateProductOrderDeliveryDate(receivedOrder,orderId);
    }

    @Operation(summary = "Used for UC6  Show Delivery Reports", description = "Needed to get the nr of days needed to deliver a order")
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
