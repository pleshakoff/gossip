package com.gossip.server.node.peers;


import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class PeerImpl implements Peer {

    final Integer id;

    @Override
    public Integer getId() {
        return this.id;
    }


}
