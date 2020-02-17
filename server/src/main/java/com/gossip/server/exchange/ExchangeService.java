package com.gossip.server.exchange;

public interface ExchangeService {
    void  gossipPull();

    ResponseGossipPullDTO gossipPullResponse(Integer id,
                                             Integer version);
}
