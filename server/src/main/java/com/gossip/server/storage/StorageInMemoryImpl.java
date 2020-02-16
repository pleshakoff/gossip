package com.gossip.server.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
class StorageInMemoryImpl implements Storage {

     private final List<Record> list = new ArrayList<>();



    @Override
    public void insert(Record record) {
        list.add(record);
    }


    @Override
    public List<Record> all() {
        return list;
    }

}
