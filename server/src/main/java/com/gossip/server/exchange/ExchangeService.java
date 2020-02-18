package com.gossip.server.exchange;

public interface ExchangeService {
    void  gossipPull();

    ResponseGossipPullDTO gossipPullResponse(String id,
                                             Integer version);
}
