package com.gossip.server.storage;

import java.util.List;

public interface StorageService {

    List<Record> all();

   Record add(String value);

    Boolean add(Record record);
}
