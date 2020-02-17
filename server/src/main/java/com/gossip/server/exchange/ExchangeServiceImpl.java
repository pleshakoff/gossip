package com.gossip.server.exchange;


import com.gossip.server.context.Context;
import com.gossip.server.node.clock.ClockVector;
import com.gossip.server.node.peers.Peer;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
class ExchangeServiceImpl implements ExchangeService {

    private final Context context;
    private final ClockVector clockVector;
    private final Http http;
    private final StorageService storageService;

    private List<ResponseGossipPullDTO> sendPullToAllPeers() {
        List<CompletableFuture<ResponseGossipPullDTO>> answerFutureList =
                context.getPeers().stream()
                        .map(Peer::getId)
                        .map(this::sendPoolForOnePeer)
                        .collect(Collectors.toList());
        return CompletableFuture.allOf(
                answerFutureList.toArray(new CompletableFuture[0])
        ).thenApply(v ->
                      answerFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList())
        ).join();
    }



    private CompletableFuture<ResponseGossipPullDTO> sendPoolForOnePeer(Integer idPeer) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.debug("Peer #{} send pull request to  {}",context.getId(), idPeer);
                MultiValueMap<String,String> params  = new LinkedMultiValueMap<>();
                params.add("id", context.getId().toString());
                params.add("version", clockVector.getPeerVersion(idPeer).toString());

                ResponseEntity<ResponseGossipPullDTO> response = http.callGet(idPeer.toString(),
                                                                              ResponseGossipPullDTO.class,
                                                                              params,
                                                                              "gossip");
                return Optional.ofNullable(response.getBody()).
                        orElse(null);
            } catch (HttpException e) {
                log.error("Peer #{} pull request error for {}. Response status code {}", context.getId(),
                          idPeer, e.getStatusCode());
                return null;
            } catch (ResourceAccessException e) {
                log.error("Peer #{} pull request error for {}. {} {} ", context.getId(), idPeer, e.getClass(),e.getMessage());
                return null;
            }
            catch (Exception e) {
                log.error(String.format("Peer #%d pull request error for %d",context.getId(), idPeer), e);
                return null;
            }
        });
    }



    @Override
    public void  gossipPull(){

        sendPullToAllPeers().stream().filter(Objects::nonNull).
                forEach(response ->
                             {
                                 log.debug("Peer #{} process request from {}",context.getId(), response.getIdPeer());

                                 if (response.getRecords().size() > 0) {
                                     clockVector.setPeerVersion(response.getIdPeer(),response.getVersion());
                                     log.info("Peer #{} get data from {} version {} record count {} ",context.getId(), response.getIdPeer(),response.getVersion(),response.getRecords().size());
                                     boolean incVersion = false;
                                     for (Record record:response.getRecords()) {
                                         incVersion = storageService.add(record)||incVersion;
                                     }
                                     if (incVersion) {
                                         clockVector.incCurrVersion();
                                     }
                                 }
                             }
                          );
    }



    @Override
    public ResponseGossipPullDTO gossipPullResponse(Integer id,
                                                    Integer oldPeerVersion){

        log.debug("Peer #{} get pull request from {} version {}",context.getId(), id,oldPeerVersion);

        context.cancelIfNotActive();

        Integer currVersion = clockVector.getCurrVersion();
        Integer peerVersion = clockVector.getPeerVersion(id);
        List<Record> records = new ArrayList<>();

        log.debug("Peer #{} check pull request. Version {}, peer version {}, current version {} ",context.getId(),oldPeerVersion,peerVersion,currVersion);
        if ((oldPeerVersion < currVersion) && (currVersion>peerVersion+1 || peerVersion==0))
        {
            log.info("Peer #{} prepare data to answer {}.  Version {}, peer version {}, current version {} ",context.getId(),id,oldPeerVersion,peerVersion,currVersion);
            List<Record> storage = storageService.all();
            for (int i = storage.size()-1; i>=0 && storage.get(i).getVersion()>oldPeerVersion; i--) {
                records.add(storage.get(i));
            }
        }
        return new ResponseGossipPullDTO(context.getId(),currVersion,records);

    }




}
