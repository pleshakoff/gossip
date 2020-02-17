package com.gossip.client.storage;

import java.util.List;

public interface StorageService {

    List<Record> all(Integer peerId);

    void add(Integer peerId,String message);





}
