package net.gabrielkovacs.showDeliveryReports.flow;

import net.gabrielkovacs.showDeliveryReports.entities.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Flow;

@Component
public class GenerateReportSubscriber implements Flow.Subscriber<QueryResponse> {

    Logger log = LoggerFactory.getLogger(GenerateReportSubscriber.class);
    private Flow.Subscription subscription;

    static QueryResponse response = new QueryResponse("placeholder", "1", null);;

    public static QueryResponse getResponse() {
        return response;
    }

    public static void setResponse(QueryResponse response) {
        GenerateReportSubscriber.response = response;
    }

    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        log.info("Subscribed");
        subscription.request(1);
    }

    public void onNext(QueryResponse message) {
        log.info("Received message: {}", message);

        this.setResponse(message);
        this.subscription.request(1);
    }

    public void onError(Throwable throwable) {
        log.info("ERROR: {}",throwable.getMessage());
    }

    public void onComplete() {
        log.info("Completed");

    }
}
