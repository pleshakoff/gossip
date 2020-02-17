package com.gossip.server.storage;

import com.gossip.server.node.Attributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
class StorageInMemoryImpl implements Storage {

     private final List<Record> list = new ArrayList<>();
    private final HashSet<String> index = new HashSet<>();
     private final Attributes attributes;

    @Override
    synchronized public void insert(Record record) {
        log.info("Peer #{} Add new record to storage {}",attributes.getId(),record.getId());
        list.add(record);
        index.add(record.getId());
    }

    @Override
    public boolean contains(String id) {
        return index.contains(id);
    }


    @Override
    public List<Record> all() {
        return list;
    }

}
