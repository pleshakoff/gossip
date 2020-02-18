package com.gossip.server.node.peers;


import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class PeerImpl implements Peer {

    final String id;

    @Override
    public String getId() {
        return this.id;
    }


}
