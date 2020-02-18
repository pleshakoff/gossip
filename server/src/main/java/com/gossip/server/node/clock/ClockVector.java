package com.gossip.server.node.clock;

import java.util.List;

public interface ClockVector {


    List<Clock> get();

    Integer getCurrVersion();

    Integer incCurrVersion();

    void decCurrVersion();

    Integer getPeerVersion(String idPeer);

    void incPeerVersion(String idPeer);

}
