package com.gossip.server.storage;

import java.util.List;

public interface StorageService {

    List<Record> all();

    void add(String value);
}
