package com.cadiducho.fem.lobby;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.lobby.Lobby;
import com.cadiducho.fem.lobby.ping.ServerPing;
import com.cadiducho.fem.lobby.ping.ServerPingOptions;
import com.cadiducho.fem.lobby.ping.ServerPingReply;
import java.io.IOException;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class ServerSign {

    private int counter;
    @Getter @Setter private String serverName;
    @Getter @Setter private String hostname;
    @Getter @Setter private int port;
    @Getter private boolean online;
    private String ID;
    public static Lobby plugin;
    @Getter private int maxPlayers = 0;
    @Getter private int players = 0;
    @Getter private String gameState = "RESTARTING";
    @Getter @Setter private String mapname;


    private static final HashMap<String, String[]> signlines = new HashMap<>();

    public static void loadSignLines() {
        if (!plugin.getConfig().contains("signs.format.WAITING_FOR_PLAYERS")) {
            plugin.getConfig().set("signs.format.WAITING_FOR_PLAYERS.lines.1", ChatColor.DARK_GREEN + "[Join]");
            plugin.getConfig().set("signs.format.WAITING_FOR_PLAYERS.lines.2", "%MAPNAME%");
            plugin.getConfig().set("signs.format.WAITING_FOR_PLAYERS.lines.3", "%SERVER%");
            plugin.getConfig().set("signs.format.WAITING_FOR_PLAYERS.lines.4", ChatColor.DARK_PURPLE + "[%PLAYERSIZE%/%MAXPLAYERS%]");
            plugin.saveConfig();
        }
        if (!plugin.getConfig().contains("signs.format.STARTING")) {
            plugin.getConfig().set("signs.format.STARTING.lines.1", ChatColor.DARK_GREEN + "[Join]");
            plugin.getConfig().set("signs.format.STARTING.lines.2", "%MAPNAME%");
            plugin.getConfig().set("signs.format.STARTING.lines.3", "%SERVER%");
            plugin.getConfig().set("signs.format.STARTING.lines.4", ChatColor.DARK_PURPLE + "[%PLAYERSIZE%/%MAXPLAYERS%]");
            plugin.saveConfig();
        }
        if (!plugin.getConfig().contains("signs.format.INGAME")) {
            plugin.getConfig().set("signs.format.INGAME.lines.1", ChatColor.RED + "[Ingame]");
            plugin.getConfig().set("signs.format.INGAME.lines.2", "%MAPNAME%");
            plugin.getConfig().set("signs.format.INGAME.lines.3", "%SERVER%");
            plugin.getConfig().set("signs.format.INGAME.lines.4", ChatColor.DARK_PURPLE + "[%PLAYERSIZE%/%MAXPLAYERS%]");
            plugin.saveConfig();
        }
        if (!plugin.getConfig().contains("signs.format.ENDING")) {
            plugin.getConfig().set("signs.format.ENDING.lines.1", ChatColor.RED + "----------");
            plugin.getConfig().set("signs.format.ENDING.lines.2", ChatColor.RED + "--ENDING--");
            plugin.getConfig().set("signs.format.ENDING.lines.3", ChatColor.RED + "----------");
            plugin.getConfig().set("signs.format.ENDING.lines.4", ChatColor.RED + "----------");
            plugin.saveConfig();
        }
        if (!plugin.getConfig().contains("signs.format.RESTARTING")) {
            plugin.getConfig().set("signs.format.RESTARTING.lines.1", ChatColor.RED + "----------");
            plugin.getConfig().set("signs.format.RESTARTING.lines.2", ChatColor.RED + "RESTARTING");
            plugin.getConfig().set("signs.format.RESTARTING.lines.3", ChatColor.RED + "----------");
            plugin.getConfig().set("signs.format.RESTARTING.lines.4", ChatColor.RED + "----------");
            plugin.saveConfig();
        }
        for (String s : plugin.getConfig().getConfigurationSection("signs.format").getKeys(false)) {
            if (s.equalsIgnoreCase("WaitingForNewGame")) {
                return;
            }
            String path = "signs.format." + s + ".";
            signlines.put(s, new String[]{plugin.getConfig().getString(path + "lines.1"), plugin.getConfig().getString(path + "lines.2"), plugin.getConfig().getString(path + "lines.3"), plugin.getConfig().getString(path + "lines.4")});
        }
    }

    public ServerSign(String ID) {
        this.ID = ID;
        String path = "servers." + ID + ".";

        setHostname(plugin.getConfig().getString(path + "hostname"));
        setPort(plugin.getConfig().getInt(path + "port"));
        setServerName(ID);
        setMapname(plugin.getConfig().getString(path + "mapname"));
        ServerPingOptions serverPingOptions = new ServerPingOptions();
        serverPingOptions.setHostname(getHostname());
        serverPingOptions.setPort(getPort());
        ServerPing serverPing = new ServerPing();
        try {
            ServerPingReply serverPingReply = serverPing.getPing(serverPingOptions);
            if ((!serverPingReply.getDescription().equalsIgnoreCase("WAITING_FOR_PLAYERS")) && (!serverPingReply.getDescription().equalsIgnoreCase("INGAME")) && (!serverPingReply.getDescription().equalsIgnoreCase("STARTING")) && (!serverPingReply.getDescription().equalsIgnoreCase("ENDING")) && (!serverPingReply.getDescription().equalsIgnoreCase("RESTARTING"))) {
                online = false;
                System.out.print("U DONT HAVE THE RIGHT MOTD IN AT THE SERVER!");
                System.out.print("LOOK AT THE SPIGOT PLUGIN PAGE!");
                System.out.print("SERVER INFO: " + getHostname() + ", " + getPort());
            }
            gameState = serverPingReply.getDescription();
            maxPlayers = serverPingReply.getPlayers().getMax();
            players = serverPingReply.getPlayers().getOnline();
            if (!online) {
                System.out.print("Server " + ID + " is back on track!");
            }
            online = true;
        } catch (IOException e) {
            if (this.online) {
                System.out.print("Server " + ID + " went offline!");
            }
            online = false;
        }
    }

    public void joinAttempt(Player player) {
        FEMServer.getUser(player).sendToServer(getServerName());
    }

    public void updateSign(Sign sign) {
        String[] strings = (String[]) signlines.get(getGameState());
        int i = 0;
        for (String string : strings) {
            sign.setLine(i, formatText(string));
            i++;
        }
        sign.update();
    }

    public boolean needsPlayers() {
        /*if (!plugin.isDynamicSystemEnabled()) {
            return true;
        }*/
        if (this.online) {
            if ((gameState.contains("WAITING")) || (gameState.equalsIgnoreCase("STARTING"))) {
                if (maxPlayers + 3 <= players) {
                    online = false;
                    return false;
                }
                return true;
            }
            online = false;
            return false;
        }
        online = false;
        return false;
    }

    private String[] formatText(String[] string) {
        String[] returnstring = string;
        returnstring[0] = formatText(string[0]);
        returnstring[1] = formatText(string[1]);
        returnstring[2] = formatText(string[2]);
        returnstring[3] = formatText(string[3]);
        return returnstring;
    }

    private String formatText(String s) {
        String returnstring = s;
        returnstring = returnstring.replaceAll("%SERVER%", getServerName());
        returnstring = returnstring.replaceAll("%PLAYERSIZE%", Integer.toString(players));
        returnstring = returnstring.replaceAll("%MAXPLAYERS%", Integer.toString(maxPlayers));
        returnstring = returnstring.replaceAll("%MAPNAME%", getMapname());
        returnstring = returnstring.replaceAll("(&([a-f0-9]))", "�2");
        return returnstring;
    }

    public void updateServer() {
        String path = "servers." + ID + ".";

        setHostname(getHostname());
        setPort(getPort());
        setServerName(ID);
        setMapname(getMapname());
        ServerPingOptions serverPingOptions = new ServerPingOptions();
        serverPingOptions.setHostname(getHostname());
        serverPingOptions.setPort(getPort());
        ServerPing serverPing = new ServerPing();
        try {
            ServerPingReply serverPingReply = serverPing.getPing(serverPingOptions);
            if ((!serverPingReply.getDescription().equalsIgnoreCase("WAITING_FOR_PLAYERS")) && (!serverPingReply.getDescription().equalsIgnoreCase("INGAME")) && (!serverPingReply.getDescription().equalsIgnoreCase("STARTING")) && (!serverPingReply.getDescription().equalsIgnoreCase("ENDING")) && (!serverPingReply.getDescription().equalsIgnoreCase("RESTARTING"))) {
                this.online = false;
                System.out.print("U DONT HAVE THE RIGHT MOTD IN AT THE SERVER!");
                System.out.print("LOOK AT THE SPIGOT PLUGIN PAGE!");
                System.out.print("SERVER INFO: " + getHostname() + ", " + getPort());
            }
            gameState = serverPingReply.getDescription();
            maxPlayers = serverPingReply.getPlayers().getMax();
            players = serverPingReply.getPlayers().getOnline();
            if (!online) {
                System.out.print("Server " + this.ID + " is back on track!");
            }
            online = true;
        } catch (IOException e) {
            if (online) {
                System.out.print("Server " + this.ID + " went offline!");
            }
            online = false;
        }
    }
}
