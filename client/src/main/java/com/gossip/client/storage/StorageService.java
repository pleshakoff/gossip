package com.gossip.client.storage;

import java.util.List;

public interface StorageService {

    List<Record> all(String peerId);

    Record add(String peerId,String message);





}
