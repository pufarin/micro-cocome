package net.gabrielkovacs.showDeliveryReports.controller;

import net.gabrielkovacs.common.models.DeliveryReport;
import org.springframework.web.bind.annotation.RestController;
import net.gabrielkovacs.showDeliveryReports.services.GenerateReportService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
class DeliveryReportController{

    private GenerateReportService generateReportService;

    public DeliveryReportController(GenerateReportService generateReportService){
        this.generateReportService = generateReportService;
    }

    @GetMapping(value="delivery-report/{enterpriseId}")
    public ResponseEntity<List<DeliveryReport>> generateDeliveryReportForEnterprise(@PathVariable long enterpriseId){
        List<DeliveryReport> queryResult = generateReportService.generateDeliveryReport(enterpriseId);

        if(queryResult.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(queryResult);
        

    }
    
    

}