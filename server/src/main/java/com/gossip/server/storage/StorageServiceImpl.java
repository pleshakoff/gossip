package com.gossip.server.storage;

import com.gossip.server.node.Attributes;
import com.gossip.server.node.clock.ClockVector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final Storage storage;
    private final ClockVector clockVector;
    private final Attributes attributes;


    @Override
    public List<Record> all() {
        return storage.all();
    }

    @Override
    public void add(String value) {
        Integer version = clockVector.incCurrVersion();
        try {
            storage.insert(new Record(UUID.randomUUID().toString(), version, LocalTime.now(), value,
                                      Collections.singletonList(attributes.getId())));
        }
        catch (Exception e){
            clockVector.decCurrVersion();
            throw e;
        }
    }

    @Override
    public Boolean add(Record record) {
        if (record.getVisited().stream().noneMatch(visitedPeerId -> visitedPeerId.equals(attributes.getId()))) {
            record.getVisited().add(attributes.getId());
            record.setVersion(clockVector.incCurrVersion());
            try {
                storage.insert(record);
            }
            catch (Exception e){
                clockVector.decCurrVersion();
                throw e;
            }
            return  true;
        }
        else {
            log.info("Peer #{} record {} already in storage",attributes.getId(), record.getId());
            return false;
        }
    }



}
