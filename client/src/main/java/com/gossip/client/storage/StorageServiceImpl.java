package com.gossip.client.storage;

import com.gossip.client.exchange.ExchangeService;
import com.network.http.Http;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final Http http;
    private final ExchangeService exchangeService;

    @Override
    public List<Record> all(Integer peerId) {
      //  exchangeService.checkAvailable(peerId);
        return http.callGet(peerId.toString(), new ParameterizedTypeReference<List<Record>>() {}, "chat").getBody();
    }

    @Override
    public void add(Integer peerId, String value) {
         exchangeService.checkAvailable(peerId);
         http.callPost(peerId.toString(),null,value,"chat");
    }



}
