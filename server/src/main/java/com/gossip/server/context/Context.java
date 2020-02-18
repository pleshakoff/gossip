package com.gossip.server.context;


import com.gossip.server.node.clock.Clock;
import com.gossip.server.node.peers.Peer;

import java.util.List;


public interface Context {

    String getId();

    void setActive(Boolean active);
    Boolean getActive();

    List<Peer> getPeers();
    Peer getPeer(String id);

    Integer getStorageSize();

    List<Clock> getClock();

    void cancelIfNotActive();

}
