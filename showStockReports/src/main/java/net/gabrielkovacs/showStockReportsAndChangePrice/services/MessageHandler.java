package net.gabrielkovacs.showStockReportsAndChangePrice.services;

import net.gabrielkovacs.showStockReportsAndChangePrice.entities.ClientCallBack;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.QueryResponse;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.ReportEntry;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.StockItemReport;
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
                log.info("Allready in the switch {}", eventName);

                List<ReportEntry> reportEntries = showStockReportsService.retrieveStockItemReportForStore(Long.parseLong(clientCallBack.getParameter()));
                String responsePayload = messageManipulation.convertListOfReportEntryToString(reportEntries);
                QueryResponse queryResponse = new QueryResponse(responsePayload,clientCallBack.getUuid(),new Timestamp( date.getTime()));
                log.info("This is the query response {} ", queryResponse.toString());
                messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponse));
               // log.info("This is the query response as string {} ",messageManipulation.convertQueryResponseToString(queryResponse));

                break;
            case("bla"):
                log.info("Bla, bla");
                break;
        }
    }
}
