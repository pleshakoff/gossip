package com.gossip.server.node.clock;

import javax.annotation.PostConstruct;
import java.util.List;

public interface ClockVector {


    List<Clock> get();

    Integer getCurrVersion();

    Integer incCurrVersion();

    Integer decCurrVersion();

    Integer getPeerVersion(Integer idPeer);

    void setPeerVersion(Integer idPeer,
                        Integer version);
}
