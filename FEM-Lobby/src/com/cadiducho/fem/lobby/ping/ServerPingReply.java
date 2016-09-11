package com.cadiducho.fem.lobby.ping;

import java.util.List;
import lombok.Getter;

public class ServerPingReply {

    @Getter private String description;
    @Getter private Players players;
    @Getter private Version version;
    @Getter private String favicon;

    public class Players {
        @Getter private int max;
        @Getter private int online;
        @Getter private List<ServerPingReply.Player> sample;
    }

    public class Player {
        @Getter private String name;
        @Getter private String id;
    }

    public class Version {
        @Getter private String name;
        @Getter private int protocol;
    }
}
