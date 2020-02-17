package com.gossip.client.context;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;


@Getter
public class Peer {

    private final Integer id;

    @JsonCreator
    public Peer(@JsonProperty("id") Integer id) {
        this.id = id;
    }
}
