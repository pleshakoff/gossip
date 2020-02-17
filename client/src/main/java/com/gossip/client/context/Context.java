package com.gossip.client.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
public class Context {

    private final Integer id;
    private final Boolean active;
    private final Integer storageSize;
    private final List<Peer> peers;
}
