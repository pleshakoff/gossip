package com.gossip.server.node.clock;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Getter
public class Clock {

    private  final String idPeer;
    private final AtomicInteger version;

}
