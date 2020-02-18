package com.gossip.client.context;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Getter
public class Clock {

    private  final Integer idPeer;
    private final AtomicInteger version;

}
