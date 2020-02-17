package com.gossip.server.node.clock;

import java.util.List;

public interface ClockVector {


    List<Clock> get();

    Integer getCurrVersion();

    Integer incCurrVersion();

    void decCurrVersion();

    Integer getPeerVersion(Integer idPeer);

    void incPeerVersion(Integer idPeer);

}
