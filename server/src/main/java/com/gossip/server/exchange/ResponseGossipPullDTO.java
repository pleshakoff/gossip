package com.gossip.server.exchange;


import com.gossip.server.storage.Record;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
class ResponseGossipPullDTO {

  private  final Integer idPeer;
  private final Integer version;
  private  final List<Record> records;

}
