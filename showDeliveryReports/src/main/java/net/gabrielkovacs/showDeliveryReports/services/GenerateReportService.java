package net.gabrielkovacs.showDeliveryReports.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import net.gabrielkovacs.showDeliveryReports.entities.ProductSupplierAndProducts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import net.gabrielkovacs.showDeliveryReports.entities.DeliveryReport;
import net.gabrielkovacs.showDeliveryReports.entities.ProductDeliveryDuration;
import net.gabrielkovacs.showDeliveryReports.repository.ProductSuplierRepository;

@Service
public class GenerateReportService {

    Logger log = LoggerFactory.getLogger(GenerateReportService.class);

    private ProductSuplierRepository productSuplierRepository;

    public GenerateReportService(ProductSuplierRepository productSuplierRepository){
        this.productSuplierRepository = productSuplierRepository;
    }

    public List<DeliveryReport>  generateDeliveryReport(ProductSupplierAndProducts productSupplierAndProducts){

        HashMap<Long, List<Long>> supplyChain = productSupplierAndProducts.getSupplyChain();
        List<DeliveryReport> toReturn = supplyChain.keySet().stream()
                .map(key-> new DeliveryReport(key,getMean(supplyChain.get(key))))
                .collect(Collectors.toList());
        return toReturn;
    }

    private double getMean(List<Long> values){
        return values.stream().mapToLong(Long::longValue).average().orElse(Double.NaN);
    }

    public ProductSupplierAndProducts generateProductSupplierAndProducts(long enterpriseId){
        ProductSupplierAndProducts productSupplierAndProducts = new ProductSupplierAndProducts();

        List<Long> productSupplierIds = productSuplierRepository.getAllProductSupliersIdsPerEnterprise(enterpriseId);
        log.debug("Product Suppliers IDs: {} ", productSupplierIds);

        if(!productSupplierIds.isEmpty()){
            for( long suplierId: productSupplierIds ){
                List<Long> productIds = productSuplierRepository.getAllProductIdsPerProductSuplier(suplierId);
                log.debug("Product Ids: {}", productIds);
                productSupplierAndProducts.addEntryToSupplyChain(suplierId, productIds);
            }
            return productSupplierAndProducts;
        }
        return new ProductSupplierAndProducts();
    }

}