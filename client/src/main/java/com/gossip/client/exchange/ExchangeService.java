package com.gossip.client.exchange;

import com.gossip.client.context.Context;

import java.util.List;

public interface ExchangeService {

    List<Context> requestContextFromAllPeers();

    void checkAvailable(Integer peerId);
}
