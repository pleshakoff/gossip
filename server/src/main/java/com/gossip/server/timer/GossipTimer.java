package com.gossip.server.timer;


import com.gossip.server.exchange.ExchangeService;
import com.gossip.server.node.Attributes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@RequiredArgsConstructor
@Component
public class GossipTimer {

    private final Attributes attributes;
    private final Timer timer = new Timer();
    private final ExchangeService exchangeService;


    @Getter
    private final AtomicInteger counter = new AtomicInteger(0);



    @Value("${gossip.timeout}" )
    Integer timeout;


    @PostConstruct
    private void start() {

        timer.schedule(new TimerTask() {
            public void run() {

                if (attributes.getActive()) {

                    counter.incrementAndGet();
                    log.debug("Peer #{} Time to next gossip: {} sec", attributes.getId(),timeout - counter.get());
                    if (counter.get() >=  timeout) {
                        counter.set(0);
                        exchangeService.gossipPull();
                    }
                }
                else
                    counter.set(0);

            }
        }, 0, 1000);
    }


}
