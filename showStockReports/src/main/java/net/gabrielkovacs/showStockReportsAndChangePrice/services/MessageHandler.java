package net.gabrielkovacs.showStockReportsAndChangePrice.services;

import net.gabrielkovacs.common.entities.StockItem;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class MessageHandler {
    Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private MessageManipulation messageManipulation;
    private ShowStockReportsService showStockReportsService;
    private MessageProducer messageProducer;

    public MessageHandler(MessageManipulation messageManipulation, ShowStockReportsService showStockReportsService,
                          MessageProducer messageProducer) {
        this.messageManipulation = messageManipulation;
        this.showStockReportsService = showStockReportsService;
        this.messageProducer = messageProducer;
    }

    public void cosumeMessage(ClientCallBack clientCallBack){
        log.info("I am in the handler,  {}", clientCallBack.toString());
        String eventName = clientCallBack.getEventName();
        Date date= new Date();
        switch (eventName){
            case("returnStockItemReports"):
                log.info("Already in the returnStockItemReports {}", eventName);

                List<ReportEntry> reportEntries = showStockReportsService.retrieveStockItemReportForStore(Long.parseLong(clientCallBack.getParameter()));
                String responsePayload = messageManipulation.convertListOfReportEntryToString(reportEntries);
                QueryResponse queryResponse = new QueryResponse(responsePayload,clientCallBack.getUuid(),new Timestamp( date.getTime()));
                log.info("This is the query response {} ", queryResponse.toString());
                messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponse));
               // log.info("This is the query response as string {} ",messageManipulation.convertQueryResponseToString(queryResponse));

                break;
            case("changeStockItemPrice"):
                log.info("Already in the changeStockItemPrice {}", clientCallBack.toString());
                StockItem stockItem = messageManipulation.convertStringToStockItemObject(clientCallBack.getParameter());
                ResponseEntity<StockItem> responseEntity = showStockReportsService.changeStockItemPrice(stockItem.getId(), stockItem.getSalePrice());
                log.info("This is the ResponseEntity {}:", responseEntity );
                String responsePayload1 = messageManipulation.convertResponseEntityToString(responseEntity);
                QueryResponse queryResponse1 = new QueryResponse(responsePayload1,clientCallBack.getUuid(),new Timestamp( date.getTime()));
                log.info("This is the query response {} ", queryResponse1.toString());
                messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponse1));

                break;
        }
    }
}
