package com.gossip.server.node.peers;


import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


@RequiredArgsConstructor
public class PeerImpl implements Peer {

    final Integer id;

    @Override
    public Integer getId() {
        return this.id;
    }


}
