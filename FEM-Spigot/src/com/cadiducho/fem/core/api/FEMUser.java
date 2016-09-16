package com.cadiducho.fem.core.api;

import com.cadiducho.fem.core.FEMCore;
import com.cadiducho.fem.core.cmds.FEMCmd.Grupo;
import com.cadiducho.fem.core.util.FEMFileLoader;
import com.cadiducho.fem.core.util.LobbyMessageTask;
import com.cadiducho.fem.core.util.Metodos;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class FEMUser {
    
    @Getter private final OfflinePlayer base;
    @Getter private final UUID uuid;
    private static final FEMCore plugin = FEMCore.getInstance();
    
    @Getter @Setter private UserData userData;
    
    public FEMUser(OfflinePlayer p) {
        base = p;
        uuid = p.getUniqueId();
        setUserData(plugin.getMysql().loadUserData(uuid));
    }
    
    public FEMUser(UUID id) {
        this(plugin.getServer().getOfflinePlayer(id));
    }

    public void save() {
        plugin.getMysql().saveUser(this);
        FEMServer.users.remove(this);
        plugin.getMysql().loadUserData(uuid);
        FEMServer.users.add(this);     
    }

    public Player getPlayer() {
        if (getBase().isOnline()) {
            return getBase().getPlayer();
        }
        return null;
    }

    public String getName() {
        return base.getName(); 
    }
    
    public boolean isOnline() { 
        return (base == null || base.isOnline()); 
    }
    
    // FEM
    public void sendMessage(String str, Object... obj) {
        String msg;
        if (str.startsWith("*")) { //Intentar reemplazar por mensaje predefinido
            msg = FEMFileLoader.getLang().getString(str.substring(1), "&o" + str);
            if (obj != null) {
                int i = 0;
                for (Object re : obj) {
                    msg = msg.replace("{" + i + "}", re.toString());
                    i++;
                }
            }
        } else msg = str;
        for (String split : msg.split("\\{n\\}")) {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                getPlayer().sendMessage(Metodos.colorizar(plugin.getTag() + " " + split));
            });
        }

    }
    
    public void sendRawMessage(String str) {
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            getPlayer().sendMessage(str);
        });
    }
    
    public boolean isOnRank(Grupo rank) {
        return rank.getRank() <= getUserData().getGrupo().getRank();
    }
    
    public void throwMe(String[] args) {
        String accion = Metodos.colorizar("&e* " + getDisplayName() + "&e ") + Metodos.buildString(args);
        plugin.getServer().broadcastMessage(accion);
    }
    
    public void toggleAdminChat() {
        if (!FEMServer.adminChatMode.contains(this)) {
            sendMessage("*adminchat.activado");
            FEMServer.adminChatMode.add(this);
        } else {
            sendMessage("*adminchat.desactivado");
            FEMServer.adminChatMode.remove(this);
        }
    }

    public void suicide() {
        EntityDamageEvent ede = new EntityDamageEvent(getPlayer(), EntityDamageEvent.DamageCause.SUICIDE, Short.MAX_VALUE);
        plugin.getServer().getPluginManager().callEvent(ede);
        getPlayer().damage(Short.MAX_VALUE);
        if (getPlayer().getHealth() > 0) {
            getPlayer().setHealth(0);
	}
        
        plugin.getServer().broadcastMessage(Metodos.colorizar(FEMFileLoader.getLang().getString("suicide.mensaje").replace("{0}", getDisplayName())));
    }
    
    public String getDisplayName() {
        if (getUserData().getNickname() != null) {
            return getUserData().getNickname();
        }
        if (getPlayer().isOnline()) {
            if (getPlayer().getDisplayName() != null) {
                return getPlayer().getDisplayName();
            }
        }
        return getPlayer().getName();
    }
    
    public void tryHidePlayers() { //SOLO LOBBIES
        switch (getUserData().getHideMode()) {
            case 0:
                plugin.getServer().getOnlinePlayers().forEach(pl -> getPlayer().hidePlayer(pl));
                break;
            case 1:
                plugin.getServer().getOnlinePlayers().stream()
                        .filter(p -> getUserData().getAmigos().contains(p.getUniqueId()))
                        .forEach(p -> getPlayer().showPlayer(p));
                plugin.getServer().getOnlinePlayers().stream()
                        .filter(p -> !getUserData().getAmigos().contains(p.getUniqueId()))
                        .forEach(p -> getPlayer().hidePlayer(p));
                break;
            case 2:
                plugin.getServer().getOnlinePlayers().forEach(pl -> getPlayer().showPlayer(pl));
                break;
        }
    }
    
    public void sendToServer(String str) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(str);
        getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray()); 
    }
    
    public void sendToLobby() {
        sendToServer("lobby"); //Próximamente cambiar y adapatar a varios lobbies
    }
    
    /*
     * Reflection
     */
    public void sendActionBar(String msg) {
        if (getPlayer() == null) return;
        try {
            Constructor<?> constructor = Metodos.getNMSClass("PacketPlayOutChat").getConstructor(Metodos.getNMSClass("IChatBaseComponent"), byte.class);
        
            Object icbc = Metodos.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + Metodos.colorizar(msg) + "\"}");
            Object packet = constructor.newInstance(icbc, (byte) 2);
            Object entityPlayer= getPlayer().getClass().getMethod("getHandle").invoke(getPlayer());
            Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

            playerConnection.getClass().getMethod("sendPacket", Metodos.getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | InstantiationException ex) {}
    }

    public void repeatActionBar(String msg) {
        new LobbyMessageTask(this, msg).runTaskTimer(plugin, 2L, 20L);
    }
    
    public int getPing() {
        try {
            Method getHandleMethod = getBase().getClass().getDeclaredMethod("getHandle");
            getHandleMethod.setAccessible(true);
            
            Object entityPlayer = getHandleMethod.invoke(getBase());
            
            Field pingField = entityPlayer.getClass().getDeclaredField("ping");
            pingField.setAccessible(true);
            
            return pingField.getInt(entityPlayer);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {}
        return -1;
    }
    
    public void addPlayed(int id, int q) {
        HashMap<Integer, Integer> plays = getUserData().plays;
        int n = plays.get(id);
        plays.remove(id);
        n = n + q;
        plays.put(id, n);
        save();
    }
    
    public void addKilled(int id, int q) {
        HashMap<Integer, Integer> kills = getUserData().kills;
        int n = kills.get(id);
        kills.remove(id);
        n = n + q;
        kills.put(id, n);
        save();
    }
    
    public void addDied(int id, int q) {
        HashMap<Integer, Integer> deaths = getUserData().deaths;
        int n = deaths.get(id);
        deaths.remove(id);
        n = n + q;
        deaths.put(id, n);
        save();
    }
    
    public void addWon(int id, int q) {
        HashMap<Integer, Integer> wins = getUserData().wins;
        int n = wins.get(id);
        wins.remove(id);
        n = n + q;
        wins.put(id, n);
        save();
    }
    
    @Data
    public static class UserData {
        //Datos
        Grupo grupo = null;
        Location lastLocation = null;
        Boolean god = null;
        Long lastConnect = null;
        Long timeJoin = null;
        String nickname = null;
        Integer coins = null;
        InetSocketAddress ip = null;
        
        //Stats
        /* IDs de juegos:
            * 1: TntWars
            * 2: DyeOrDie
            * 3: GemHunters
        */
        HashMap<Integer, Integer> kills = new HashMap<>(); //id Juego, cantidad
        HashMap<Integer, Integer> deaths = new HashMap<>();
        HashMap<Integer, Integer> wins = new HashMap<>();
        HashMap<Integer, Integer> plays = new HashMap<>();
        Integer tntPuestas = null;
        Integer tntQuitadas = null;
        Integer tntExplotadas = null;
        Integer genUpgraded = null;
        Integer gemDestroyed = null;
        Integer gemPlanted = null;
        Integer record_dod = null;
        Integer rondas_dod = null;

        //Settings
        Boolean friendRequest = null;
        Integer hideMode = null; //0 nadie, 1 amigos, 2 todos
        
        
        ArrayList<UUID> amigos = null;
    }
    //-----
    
    @Override
    public String toString() {
        return "FEMUser{" + getName() + "}";
    }
 }
