package net.gabrielkovacs.showDeliveryReports.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.gabrielkovacs.showDeliveryReports.entities.ServiceBusMessageCommand;
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

    private final String baseUri = "http://localhost:8083";
    private final String getDeliveryTimePerProductId = "product-order/delivery-time";

    private WebClient webClient = WebClient.create(baseUri);

    private ProductSuplierRepository productSuplierRepository;
    private MessageManipulation messageManipulation;

    public GenerateReportService(ProductSuplierRepository productSuplierRepository,
                                 MessageManipulation messageManipulation){
        this.productSuplierRepository = productSuplierRepository;
        this.messageManipulation = messageManipulation;
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

    public String generateCorrelationId(){
        return UUID.randomUUID().toString();
    }

    public ServiceBusMessageCommand generateGetDeliveryTimePerProductCommandMessage(String correlationId,
                                                                                    List<Long> productId,
                                                                                    Timestamp timestamp, String sender){
        String command = generateGetDeliveryTimePerProductCommand(productId);
        return new ServiceBusMessageCommand(correlationId, command, timestamp, sender );
    }

    private String generateGetDeliveryTimePerProductCommand(List<Long> productId){
        String productIds = messageManipulation.convertListOfProductIdsToString(productId);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", "getDeliveryDurationPerProduct");
        jsonObject.addProperty("productId", productIds);

        return new Gson().toJson(jsonObject);
    }
}