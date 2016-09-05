package com.cadiducho.fem.lobby;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

public class LobbySign {
    
    @Getter @Setter private String rawname;
    @Getter @Setter private String status;
    @Getter @Setter private String players;
    @Getter @Setter private Location loc;
    
    public String getName() {
        if (rawname.contains("tntwars")) return "TNTWars";
        else if (rawname.contains("dyd")) return "Dye or Die";
        else if (rawname.contains("gemhun")) return "Gem Hunters";
        else return "Desconocido";
    }
    
      
    public static LobbySign getLobbySignByName(String name) {
        for (LobbySign sign : Lobby.getInstance().getSigns()) {
            if (sign.getRawname().equalsIgnoreCase(name)) return sign;
        }
        return null;
    }
    
    public static LobbySign getLobbySignByLoc(Location loc) {
        for (LobbySign sign : Lobby.getInstance().getSigns()) {
            if (sign.getLoc().getBlockX() == loc.getBlockX() &&
                    sign.getLoc().getBlockY() == loc.getBlockY() &&
                    sign.getLoc().getBlockZ() == loc.getBlockZ()) {
                return sign;
            }
        }
        return null;
    }
    
}
