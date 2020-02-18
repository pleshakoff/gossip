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
    public List<Record> all(String peerId) {
      //  exchangeService.checkAvailable(peerId);
        return http.callGet(peerId.toString(), new ParameterizedTypeReference<List<Record>>() {}, "chat").getBody();
    }

    @Override
    public Record add(String peerId, String value) {
         exchangeService.checkAvailable(peerId);
         return http.callPost(peerId.toString(),Record.class,value,"chat").getBody();
    }



}
