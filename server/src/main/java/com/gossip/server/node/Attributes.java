package com.gossip.server.node;

public interface Attributes {
    String getId();

    Boolean getActive();

    void setActive(Boolean active);

    void cancelIfNotActive();
}
