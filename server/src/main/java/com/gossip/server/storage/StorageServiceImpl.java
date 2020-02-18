package com.gossip.server.storage;

import com.gossip.server.node.Attributes;
import com.gossip.server.node.clock.ClockVector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final Storage storage;
    private final ClockVector clockVector;
    private final Attributes attributes;


    @Override
    public List<Record> all() {
        return storage.all().stream().sorted(Comparator.comparing(Record::getTimestamp)).collect(
                Collectors.toList());
    }

    @Override
    synchronized public Record add(String value) {
        Integer version = clockVector.incCurrVersion();
        try {
            Record record = new Record(UUID.randomUUID().toString(), version, LocalDateTime.now(), value,
                                       Collections.singletonList(attributes.getId()));
            storage.insert(record);
            return record;
        }
        catch (Exception e){
            clockVector.decCurrVersion();
            throw e;
        }
    }

    @Override
    synchronized public Boolean add(Record record) {
        if (record.getVisited().stream().noneMatch(visitedPeerId -> visitedPeerId.equals(attributes.getId()))
           &&!storage.contains(record.getId()))
        {
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
            log.debug("Peer #{} record {} already in storage",attributes.getId(), record.getId());
            return false;
        }
    }



}
