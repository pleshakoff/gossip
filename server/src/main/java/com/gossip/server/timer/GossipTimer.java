package com.gossip.server.timer;


import com.gossip.server.node.Attributes;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class GossipTimer {

    private final Attributes attributes;
    private final Timer timer = new Timer();


    @Getter
    private final AtomicInteger counter = new AtomicInteger(0);

    protected GossipTimer(Attributes attributes) {
        this.attributes = attributes;
    }

    @Value("{gossip.api.version}" )
    Integer timeout;


    public void reset() {
        counter.set(0);
    }


    @PostConstruct
    private void start() {

        timer.schedule(new TimerTask() {
            public void run() {
                    counter.incrementAndGet();
                    log.debug("Peer #{} Time to next gossip: {} sec", attributes.getId(),timeout - counter.get());
                    if (counter.get() >=  timeout) {
                        counter.set(0);

                    }
            }
        }, 0, 1000);
    }


}
