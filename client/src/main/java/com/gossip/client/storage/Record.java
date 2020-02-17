package com.gossip.client.storage;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class Record {

    private final String id;
    private Integer version;
    private final LocalDateTime timestamp;
    private final String message;
    private final List<Integer> visited;
}
