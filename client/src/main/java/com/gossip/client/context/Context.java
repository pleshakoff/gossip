package com.gossip.client.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
public class Context {

    private final String id;
    private final Boolean active;
    private final Integer storageSize;
    List<Clock> clock;
    private final List<Peer> peers;

}
