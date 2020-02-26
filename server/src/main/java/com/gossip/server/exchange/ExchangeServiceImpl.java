package com.gossip.server.exchange;


import com.gossip.server.node.Attributes;
import com.gossip.server.node.clock.ClockVector;
import com.gossip.server.node.peers.Peers;
import com.gossip.server.storage.Record;
import com.gossip.server.storage.StorageService;
import com.network.http.Http;
import com.network.http.HttpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
class ExchangeServiceImpl implements ExchangeService {

    private final Attributes attributes;
    private final Peers peers;
    private final ClockVector clockVector;
    private final Http http;
    private final StorageService storageService;



    private CompletableFuture<ResponseGossipPullDTO> sendPullForOnePeer(String idPeer) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.debug("Peer #{} send pull request to  {}", attributes.getId(), idPeer);
                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("id", attributes.getId());
                params.add("version", clockVector.getPeerVersion(idPeer).toString());

                ResponseEntity<ResponseGossipPullDTO> response = http.callGet(idPeer,
                        ResponseGossipPullDTO.class,
                        params,
                        "gossip");
                return Optional.ofNullable(response.getBody()).orElse(null);
            } catch (HttpException e) {
                log.error("Peer #{} pull request error for {}. Response status code {}", attributes.getId(),
                        idPeer, e.getStatusCode());
                return null;
            } catch (ResourceAccessException e) {
                log.error("Peer #{} pull request error for {}. {} {} ", attributes.getId(), idPeer, e.getClass(),
                        e.getMessage());
                return null;
            } catch (Exception e) {
                log.error(String.format("Peer #%d pull request error for %d", attributes.getId(), idPeer), e);
                return null;
            }
        });
    }


    @Override
    public void gossipPull() {
        ResponseGossipPullDTO response = sendPullForOnePeer(peers.getRandom().getId()).join();
        if (response != null) {
            String idPeer = response.getIdPeer();
            log.debug("Peer #{} process request from {}", attributes.getId(), idPeer);
            if (response.getRecords().size() > 0) {
                log.debug("Peer #{} get data from {} version {} record count {} ", attributes.getId(),
                        idPeer, response.getVersion(), response.getRecords().size());
                boolean incVersion = false;
                for (Record record : response.getRecords()) {
                    if (storageService.add(record)) {
                        clockVector.incPeerVersion(idPeer);
                        incVersion = true;
                    }
                }
                if (incVersion) {
                    clockVector.incCurrVersion();
                }
                while (clockVector.getPeerVersion(idPeer) < Math.min(response.getVersion(), clockVector.getCurrVersion() - 1))
                    clockVector.incPeerVersion(idPeer);
            }
        }
    }


    @Override
    public ResponseGossipPullDTO gossipPullResponse(String id,
                                                    Integer oldPeerVersion) {

        log.debug("Peer #{} get pull request from {} version {}", attributes.getId(), id, oldPeerVersion);
        attributes.cancelIfNotActive();

        Integer currVersion = clockVector.getCurrVersion();
        Integer peerVersion = clockVector.getPeerVersion(id);

        List<Record> records = new ArrayList<>();

        log.debug("Peer #{} check pull request. Version {}, peer version {}, current version {} ", attributes.getId(),
                oldPeerVersion, peerVersion, currVersion);
        if ((oldPeerVersion < currVersion) && (currVersion > peerVersion + 1 || peerVersion == 0)) {
            log.debug("Peer #{} prepare data to answer {}.  Version {}, peer version {}, current version {} ",
                    attributes.getId(), id, oldPeerVersion, peerVersion, currVersion);
            List<Record> storage = storageService.all();
            for (int i = storage.size() - 1; i >= 0 && storage.get(i).getVersion() > oldPeerVersion; i--) {
                records.add(storage.get(i));
            }
        }
        return new ResponseGossipPullDTO(attributes.getId(), currVersion, records);

    }


}
