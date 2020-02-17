package com.gossip.server.node.clock;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Getter
class Clock {

    private  final Integer idPeer;
    private final AtomicInteger version;

}
