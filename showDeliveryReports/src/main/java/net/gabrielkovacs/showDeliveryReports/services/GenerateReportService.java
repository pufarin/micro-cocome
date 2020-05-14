package net.gabrielkovacs.showDeliveryReports.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import net.gabrielkovacs.common.models.DeliveryReport;
import net.gabrielkovacs.showDeliveryReports.models.ProductDeliveryDuration;
import net.gabrielkovacs.common.repository.ProductSuplierRepository;

@Service
public class GenerateReportService {

    Logger log = LoggerFactory.getLogger(GenerateReportService.class);

    private final String baseUri = "http://localhost:8083";
    private final String getDeliveryTimePerProductId = "product-order/delivery-time";

    private WebClient webClient = WebClient.create(baseUri);

    private ProductSuplierRepository productSuplierRepository;

    public GenerateReportService(ProductSuplierRepository productSuplierRepository){
        this.productSuplierRepository = productSuplierRepository;
    }

    public List<DeliveryReport> generateDeliveryReport(long enterpriseId){
        List<DeliveryReport> deliveryReport = new ArrayList<DeliveryReport>();
        List<Long> productSupplierIds = productSuplierRepository.getAllProductSupliersIdsPerEnterprise(enterpriseId);
        log.debug("Product Suppliers IDs: {} ", productSupplierIds);
        if(!productSupplierIds.isEmpty()){
            for( long suplierId: productSupplierIds ){
                List<Long> productIds = productSuplierRepository.getAllProductIdsPerProductSuplier(suplierId);
                log.debug("Product Ids: {}", productIds);
                if(!productIds.isEmpty()){
                    ResponseEntity<List<ProductDeliveryDuration>> productDeliveryDuration = getDeliveryTimePerProductId(productIds);
                    log.debug("Product Delivery Duration: {}", productDeliveryDuration);
                    if(productDeliveryDuration.getStatusCode().equals(HttpStatus.OK)){
                        deliveryReport.add(new DeliveryReport(suplierId, getMeanTime(productDeliveryDuration.getBody())));
                    }

                }

            }
 
            return deliveryReport;
        }
   
       return List.of(); 


    }
    
       
    
    private ResponseEntity<List<ProductDeliveryDuration>> getDeliveryTimePerProductId(List<Long> productId) {

        return webClient.get()
                        .uri(uriBuilder -> uriBuilder.path(getDeliveryTimePerProductId).queryParam("productsId", productId).build())
                        .exchange()
                        .flatMap(response -> response.toEntityList(ProductDeliveryDuration.class))
                        .block();
    }
/*
    private double getMeanTime(List<Long> nrDays){
        double mean = nrDays.stream().mapToLong(Long::longValue).average().orElse(Double.NaN);
        return mean;
    }
*/
    private double getMeanTime(List<ProductDeliveryDuration> productDeliveryDurations){
        double mean = productDeliveryDurations.stream().map(pDD -> pDD.getNrDays()).mapToLong(Long::longValue).average().orElse(Double.NaN);;
        
        return mean;
    }

}