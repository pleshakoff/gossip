package com.gossip.server.node.peers;


import com.network.Service;
import com.network.ServicesProps;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
class PeersImpl implements Peers {
    private final ServicesProps servicesProps;

    @Getter
    private final List<Peer> peers = new ArrayList<>();

    private void add(String id) {
        peers.add(new PeerImpl(id));
    }


    @Override
    public  Peer get(String id) {
        return peers.stream().
                filter(peer -> peer.getId().equals(id)).
                findFirst().
                orElseThrow(() -> new RuntimeException(String.format("Unsupported peer Id %s",id)));
    }

    @PostConstruct
     void init() {
        servicesProps.getServices().stream().
                map(Service::getName).
                forEach(this::add);
    }
}
