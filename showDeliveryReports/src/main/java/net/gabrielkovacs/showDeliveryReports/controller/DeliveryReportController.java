package net.gabrielkovacs.showDeliveryReports.controller;

import net.gabrielkovacs.showDeliveryReports.flow.GenerateReportSubscriber;
import net.gabrielkovacs.showDeliveryReports.services.MessageManipulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import net.gabrielkovacs.showDeliveryReports.entities.DeliveryReport;
import net.gabrielkovacs.showDeliveryReports.services.GenerateReportService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
class DeliveryReportController{

    Logger log = LoggerFactory.getLogger(DeliveryReportController.class);

    private GenerateReportService generateReportService;
    private GenerateReportSubscriber generateReportSubscriber;
    private MessageManipulation messageManipulation;

    public DeliveryReportController(GenerateReportService generateReportService, GenerateReportSubscriber generateReportSubscriber, MessageManipulation messageManipulation){
        this.generateReportService = generateReportService;
        this.generateReportSubscriber = generateReportSubscriber;
        this.messageManipulation = messageManipulation;
    }

    @GetMapping(value="delivery-report/{enterpriseId}")
    public ResponseEntity<List<DeliveryReport>> generateDeliveryReportForEnterprise(@PathVariable long enterpriseId) throws InterruptedException {

        String correlationId = generateReportService.generateCorrelationId();
        generateReportService.requestDeliveryReport(enterpriseId, correlationId);

        while (!correlationId.equals(generateReportSubscriber.getResponse().getUuid())){
            log.info("The correlationId: {}, theOtherOne {}", correlationId, generateReportSubscriber.getResponse().getUuid());
            Thread.sleep(5);
        }

        if(generateReportSubscriber.getResponse().getPayload().equals("NOT FOUND")){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            List<DeliveryReport> deliveryReport = messageManipulation.convertJsonToDeliveryReport(generateReportSubscriber.getResponse().getPayload());
            return ResponseEntity.ok().body(deliveryReport);
        }

    }

}