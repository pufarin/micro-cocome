package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.controller;


import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.OrderEntry;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services.OrderProductService;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
public class CreateAndReceiveOrderController {

    private OrderProductService orderProductService;

    public CreateAndReceiveOrderController(OrderProductService orderProductService){
        this.orderProductService = orderProductService;
    }


    @PostMapping("store/{storeId}/order")
    public OrderEntry orderProduct(@RequestBody OrderEntry orderEntry, @PathVariable Long storeId){
        return orderProductService.orderProduct(orderEntry,storeId);
    }


}
