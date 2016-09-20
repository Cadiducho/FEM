package com.cadiducho.fem.core.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.cadiducho.fem.core.util.JsonConfig;
import com.cadiducho.fem.core.util.FEMFileLoader;
import com.cadiducho.fem.core.util.Metodos;
import static com.cadiducho.fem.core.util.Metodos.plugin;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class FEMServer {

    final static Gson gson = new Gson();

    public static ArrayList<FEMUser> users = new ArrayList<>();
    public static ArrayList<FEMUser> adminChatMode = new ArrayList<>();
    public static ArrayList<FEMUser> afkMode = new ArrayList<>();

    private static HashMap<UUID, UUID> tp = new HashMap<>();
    private static HashMap<UUID, UUID> tph = new HashMap<>();
    private static ArrayList<Warp> warps = null;

    //Teams -> Colores de TabList
    public static Scoreboard board;
    public static Team tInvitados;
    public static Team tMiembros;
    public static Team tVips;
    public static Team tModeradores;
    public static Team tAdmins;
    public static Team tVanished;

    public static FEMUser getUser(UUID id) {
        for (FEMUser u : users) {
            if (u.getUuid() == null) {
                continue;
            }
            if (u.getUuid().equals(id)) {
                return u;
            }
        }
        FEMUser us = new FEMUser(id);
        if (us.getBase().isOnline()) {
            users.add(us);
        }
        return us;
    }

    public static FEMUser getUser(OfflinePlayer p) {
        for (FEMUser u : users) {
            if (u.getUuid() == null) {
                continue;
            }
            if (u.getUuid().equals(p.getUniqueId())) {
                return u;
            }
        }
        FEMUser us = new FEMUser(p);
        if (us.getBase().isOnline()) {
            users.add(us);
        }
        return us;
    }

    public static void sendStatus(String serverId, String status, String players) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("serverstatus");
        out.writeUTF(serverId);
        out.writeUTF(status);
        out.writeUTF(players);
        Collection<? extends Player> c = Bukkit.getOnlinePlayers(); //Usar jugador falso
        if (!c.isEmpty()) {
            Player p = (Player) c.toArray()[0];
            if (p != null) p.sendPluginMessage(plugin, "FEM", out.toByteArray());
        }
    }

    public static double[] getRecentTps() {
        Object minecraftServer;
        Field recentTps;
        try {
            Server server = Bukkit.getServer();
            Field consoleField = server.getClass().getDeclaredField("console");
            consoleField.setAccessible(true);
            minecraftServer = consoleField.get(server);
            recentTps = minecraftServer.getClass().getSuperclass().getDeclaredField("recentTps");
            recentTps.setAccessible(true);
            return (double[]) recentTps.get(minecraftServer);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
        }
        return new double[]{20, 20, 20};
    }

    public static String getFormatedTPS() {
        double tps1 = getRecentTps()[0], tps2 = getRecentTps()[1], tps3 = getRecentTps()[2];
        return "&bTPS en los últimos 1, 5 y 15m: " + format(tps1) + ", " + format(tps2) + ", " + format(tps3);
    }

    private static String format(double tps) {
        return ((tps > 18.0) ? "&a" : (tps > 16.0) ? "&e" : "&c")
                + ((tps > 20.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
    }

    public static HashMap<UUID, UUID> getTeleportRequests() {
        return tp;
    }

    public static HashMap<UUID, UUID> getTeleportHereRequests() {
        return tph;
    }

    public static void addTeleportRequest(UUID u1, UUID u2) {
        tp.put(u1, u2);
    }

    public static void addTeleportHereRequest(UUID u1, UUID u2) {
        tph.put(u1, u2);
    }

    public static void removeTeleportRequest(UUID u) {
        tp.remove(u);
    }

    public static void removeTeleportHereRequest(UUID u) {
        tph.remove(u);
    }

    @Data
    @AllArgsConstructor
    public static class Warp {

        private String name;
        private String desc;
        private String location;
        private String icon;

        public void teleport(FEMUser u) {
            u.getPlayer().teleport(Metodos.stringToLocation(getLocation()), PlayerTeleportEvent.TeleportCause.COMMAND);
            u.sendMessage("&6Teleportando a &e" + getName() + "&6...");
        }
    }

    public static ArrayList<Warp> getWarps() {
        if (warps != null) {
            return warps;
        }
        warps = new ArrayList<>();
        JsonConfig conf = new JsonConfig(FEMFileLoader.fWarps);
        if (!conf.contains("warps")) {
            return warps;
        }

        Type arrayWarps = new TypeToken<ArrayList<Warp>>() {}.getType();
        warps = gson.fromJson(conf.getString("warps"), arrayWarps);
        return warps;
    }

    public static void setWarps(ArrayList<Warp> nh) {
        warps = nh;
        JsonConfig conf = new JsonConfig(FEMFileLoader.fWarps);
        conf.set("warps", null);
        Type arrayWarps = new TypeToken<ArrayList<Warp>>() {}.getType();
        conf.set("warps", gson.toJson(warps, arrayWarps));
        conf.save();
    }

    public static ArrayList<String> getWarpNames() {
        ArrayList<String> h = new ArrayList<>();
        getWarps().forEach(w -> h.add(w.getName()));
        return h;
    }

    public static void addWarp(Warp w) {
        ArrayList<Warp> h = getWarps();
        h.add(w);
        setWarps(h);
    }

    public static void removeWarp(Warp w) {
        ArrayList<Warp> h = getWarps();
        h.remove(w);
        setWarps(h);
    }

    public static Warp getWarp(String s) {
        for (Warp w : getWarps()) {
            if (w.getName().equalsIgnoreCase(s)) {
                return w;
            }
        }
        return null;
    }

    public static void clearWarps() {
        setWarps(new ArrayList<>());
    } 
}
