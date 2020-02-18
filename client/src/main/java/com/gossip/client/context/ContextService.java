package com.gossip.client.context;

import java.util.List;

public interface ContextService {

    void stop(String peerId);

    void start(String peerId);

    List<Context> all();


}
