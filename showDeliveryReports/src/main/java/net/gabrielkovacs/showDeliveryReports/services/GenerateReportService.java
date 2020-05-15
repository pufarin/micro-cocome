package net.gabrielkovacs.showDeliveryReports.services;

import net.gabrielkovacs.common.models.DeliveryReport;
import net.gabrielkovacs.common.models.ProductDeliveryDuration;
import net.gabrielkovacs.common.repository.ProductOrderRepository;
import net.gabrielkovacs.common.repository.ProductSuplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenerateReportService {

    Logger log = LoggerFactory.getLogger(GenerateReportService.class);

    private ProductSuplierRepository productSuplierRepository;
    private ProductOrderRepository productOrderRepository;

    public GenerateReportService(ProductSuplierRepository productSuplierRepository, ProductOrderRepository productOrderRepository){
        this.productSuplierRepository = productSuplierRepository;
        this.productOrderRepository = productOrderRepository;
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
                    List<ProductDeliveryDuration> productDeliveryDuration = getDeliveryTimePerProductId(productIds);
                    log.debug("Product Delivery Duration: {}", productDeliveryDuration);
                    if(!productDeliveryDuration.isEmpty()){
                        deliveryReport.add(new DeliveryReport(suplierId, getMeanTime(productDeliveryDuration)));
                    }

                }

            }
 
            return deliveryReport;
        }
   
       return List.of(); 


    }

    private List<ProductDeliveryDuration> getDeliveryTimePerProductId(List<Long> productId) {
        return productOrderRepository.getNrDaysPerProductDelivery(productId);
    }

    private double getMeanTime(List<ProductDeliveryDuration> productDeliveryDurations){
        double mean = productDeliveryDurations.stream().map(pDD -> pDD.getNrDays()).mapToLong(Long::longValue).average().orElse(Double.NaN);;
        
        return mean;
    }

}