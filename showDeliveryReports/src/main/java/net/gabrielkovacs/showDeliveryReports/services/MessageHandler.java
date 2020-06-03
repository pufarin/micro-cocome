package net.gabrielkovacs.showDeliveryReports.services;

import net.gabrielkovacs.showDeliveryReports.entities.ClientCallBack;
import net.gabrielkovacs.showDeliveryReports.entities.DeliveryReport;
import net.gabrielkovacs.showDeliveryReports.entities.ProductSupplierAndProducts;
import net.gabrielkovacs.showDeliveryReports.entities.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class MessageHandler {
    Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private MessageManipulation messageManipulation;
    private MessageProducer messageProducer;
    private GenerateReportService generateReportService;

    public MessageHandler(MessageManipulation messageManipulation, MessageProducer messageProducer, GenerateReportService generateReportService) {
        this.messageManipulation = messageManipulation;
        this.messageProducer = messageProducer;
        this.generateReportService = generateReportService;
    }

    public void cosumeMessage(ClientCallBack clientCallBack){
        log.info("I am in the handler,  {}", clientCallBack.toString());
        String eventName = clientCallBack.getEventName();
        Date date= new Date();
        switch (eventName){
            case("generateProductSupplierAndProducts"):
                log.info("Already in the generateProductSupplierAndProducts {}", eventName);

                Long enterpriseId = Long.parseLong(clientCallBack.getParameter());
                ProductSupplierAndProducts productSupplierAndProducts = generateReportService.generateProductSupplierAndProducts(enterpriseId);
                String responsePayload = messageManipulation.convertProductSupplierAndProductsToString(productSupplierAndProducts);
                QueryResponse queryResponse = new QueryResponse(responsePayload,clientCallBack.getUuid(),new Timestamp( date.getTime()), eventName);
                log.info("This is the query response {} ", queryResponse.toString());

                messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponse));

                break;
            case("generateDeliveryReport"):
                log.info("Already in the generateDeliveryReport {}", eventName);
                ProductSupplierAndProducts duration = messageManipulation.convertStringToProductSupplierAndProducts(clientCallBack.getParameter());

                List<DeliveryReport> deliveryReports = generateReportService.generateDeliveryReport(duration);
                String responsePayload1 = messageManipulation.convertListOfDeliveryReportsToString(deliveryReports);
                QueryResponse queryResponse1 = new QueryResponse(responsePayload1,clientCallBack.getUuid(),new Timestamp( date.getTime()), eventName);
                log.info("This is the query response {} ", queryResponse1.toString());

                messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponse1));

                break;
        }
    }
}
