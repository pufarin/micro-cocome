package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.controller;

import io.swagger.v3.oas.annotations.Operation;
import net.gabrielkovacs.common.entities.OrderEntry;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ReceivedOrder;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.StockItem;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services.OrderProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("stockitem")
    public ResponseEntity<StockItem> getStuff() {
        this.orderProductService.setWebClientBaseUri(this.baseUri);
        return orderProductService.getStockItem(200, 100);
    }
}
