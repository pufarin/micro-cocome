package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Flow;

@Component
public class OrderProcessingSubscriber implements Flow.Subscriber<String> {

    Logger log = LoggerFactory.getLogger(OrderProcessingSubscriber.class);
    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(String s) {
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        log.info("All Processing Done");
    }
}
