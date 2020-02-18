package com.gossip.server.node;


import com.gossip.server.exceptions.NotActiveException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
class AttributesImpl implements Attributes {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${gossip.id}")
    @Getter
    private String id;


    @Getter
    Boolean active = true;

    @Override
    public void setActive(Boolean active) {
        this.active = active;
        log.info("Peer #{} {}", getId(),active?"STARTED":"STOPPED");
    }

    @Override
    public void cancelIfNotActive() {
        if (!getActive())
            throw  new NotActiveException();
    }





}
