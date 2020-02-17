package com.gossip.server.storage;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class Record {

    private final String id;

    @Setter
    private Integer version;
    private final LocalTime timestamp;
    private final String message;
    private final List<Integer> visited;
}
