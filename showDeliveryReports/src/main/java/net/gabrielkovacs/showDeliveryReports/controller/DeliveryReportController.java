package net.gabrielkovacs.showDeliveryReports.controller;

import org.springframework.web.bind.annotation.*;

import net.gabrielkovacs.common.models.DeliveryReport;
import net.gabrielkovacs.common.models.ProductSupplierAndProducts;
import net.gabrielkovacs.showDeliveryReports.services.GenerateReportService;

import java.util.List;

import org.springframework.http.ResponseEntity;


@RestController
class DeliveryReportController{

    private GenerateReportService generateReportService;

    public DeliveryReportController(GenerateReportService generateReportService){
        this.generateReportService = generateReportService;
    }

    @GetMapping(value="delivery-report/supplier-and-products/{enterpriseId}")
    public ResponseEntity<ProductSupplierAndProducts> generateProductSupplierAndProductsForEnterprise(@PathVariable long enterpriseId){
        ProductSupplierAndProducts queryResult = generateReportService.generateProductSupplierAndProducts(enterpriseId);
        return ResponseEntity.ok().body(queryResult);
    }

    @PostMapping(value="delivery-report/generation")
    public ResponseEntity<List<DeliveryReport>> generateDeliveryReport(@RequestBody ProductSupplierAndProducts productSupplierAndProducts){
        List<DeliveryReport> deliveryReports = generateReportService.generateDeliveryReport(productSupplierAndProducts);
        return ResponseEntity.ok().body(deliveryReports);
    }

}