package com.gossip.server.context;


import com.gossip.server.node.Attributes;
import com.gossip.server.node.clock.Clock;
import com.gossip.server.node.clock.ClockVector;
import com.gossip.server.node.peers.Peer;
import com.gossip.server.node.peers.Peers;
import com.gossip.server.storage.Storage;
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
    private  final ClockVector  clockVector;
    private  final Storage storage;

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

    @Override
    public List<Clock> getClock() {
        return clockVector.get();
    }

    @Override
    public void cancelIfNotActive() {
        attributes.cancelIfNotActive();
    }

    @Override
    public Integer getStorageSize()  {
        return storage.all().size();
    }


}
