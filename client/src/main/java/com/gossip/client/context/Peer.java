package com.gossip.client.context;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;


@Getter
public class Peer {

    private final String id;

    @JsonCreator
    public Peer(@JsonProperty("id") String id) {
        this.id = id;
    }
}
