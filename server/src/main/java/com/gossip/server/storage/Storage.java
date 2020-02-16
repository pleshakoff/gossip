package com.gossip.server.storage;

import java.util.List;

public interface Storage {

    List<Record> all();

    void insert(Record record);

}
