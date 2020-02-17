package com.gossip.server.node.clock;


import com.gossip.server.node.Attributes;
import com.gossip.server.node.peers.Peers;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClockVectorImpl implements ClockVector {


    private final List<Clock> clockList = new ArrayList<>();
    private final Attributes attributes;
    private final Peers peers;

    @PostConstruct
    public void init() {
        clockList.add(new Clock(attributes.getId(), new AtomicInteger(0)));
        peers.getPeers().forEach(peer ->
                                         clockList.add(new Clock(peer.getId(), new AtomicInteger(0)))
        );
    }

    @Override
    public List<Clock> get() {
        return clockList;
    }

    @Override
    public Integer getCurrVersion() {
        return clockList.get(0).getVersion().get();
    }

    @Override
    public Integer incCurrVersion() {
        int version = clockList.get(0).getVersion().incrementAndGet();
        log.info("Peer #{} current version incremented to {}",attributes.getId(),version);
        return version;
    }

    @Override
    public Integer decCurrVersion() {
        int version = clockList.get(0).getVersion().decrementAndGet();
        log.info("Peer #{} current version decremented to {}",attributes.getId(),version);
        return version;

    }

    @Override
    public Integer getPeerVersion(Integer idPeer) {
        return clockList.stream().filter(clock -> clock.getIdPeer().equals(idPeer)).findFirst().orElseThrow(
                () -> new RuntimeException("Unknown id peer")).getVersion().get();
    }

    @Override
    public void setPeerVersion(Integer idPeer,
                               Integer version) {
        clockList.stream().filter(clock -> clock.getIdPeer().equals(idPeer)).findFirst().orElseThrow(
                () -> new RuntimeException("Unknown id peer")).getVersion().set(version);
        log.info("Peer #{} version set to {} for peer {}",attributes.getId(),version,idPeer);

    }


}
