package com.gossip.client.context;

import com.network.http.Http;
import com.gossip.client.exchange.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
class ContextServiceImpl implements ContextService {


    private final ExchangeService exchangeService;
    private final Http http;


    @Override
    public void start(String peerId) {
        http.callPost(peerId.toString(),null,null,"context","start");
    }

    @Override
    public void stop(String peerId) {
        exchangeService.checkAvailable(peerId);
        http.callPost(peerId.toString(),null,null,"context","stop");
    }

    @Override
    public List<Context> all() {
        return exchangeService.requestContextFromAllPeers();
    }


}
