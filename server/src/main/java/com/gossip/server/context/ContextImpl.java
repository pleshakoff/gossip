package com.gossip.server.context;


import com.gossip.server.node.Attributes;
import com.gossip.server.node.peers.Peer;
import com.gossip.server.node.peers.Peers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
class ContextImpl implements Context {

    private final Peers peers;
    private final Attributes attributes;

    @Override
    public Integer getId() {
        return attributes.getId();
    }

    @Override
    public void setActive(Boolean active) {
        attributes.setActive(active);
    }

    @Override
    public Boolean getActive() {
        return attributes.getActive();
    }

    @Override
    public List<Peer> getPeers() {
        return peers.getPeers();
    }

    @Override
    public Peer getPeer(Integer id)  {
        return  peers.get(id);
    }







}
