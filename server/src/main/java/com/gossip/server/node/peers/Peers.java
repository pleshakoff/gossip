package com.gossip.server.node.peers;

public interface Peers {

    Peer get(Integer id);

    java.util.List<Peer> getPeers();

}
