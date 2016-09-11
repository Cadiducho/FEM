package com.cadiducho.fem.lobby.ping;

import lombok.Getter;
import lombok.Setter;

public class ServerPingOptions {

    @Getter @Setter private String hostname;
    @Getter @Setter private int port = 25565;
    @Getter @Setter private int timeout = 2000;
    @Getter @Setter private String charset = "UTF-8";
    
}
