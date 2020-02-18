package com.gossip.server.node.peers;

import java.util.List;

public interface Peers {

    Peer get(String id);

    List<Peer> getPeers();

}
