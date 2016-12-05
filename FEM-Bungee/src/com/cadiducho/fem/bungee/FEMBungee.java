package com.cadiducho.fem.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class FEMBungee extends Plugin implements Listener {

    String pluginChannel = "FEM";

    @Override
    public void onEnable() {
        getProxy().registerChannel(pluginChannel);
        getProxy().getPluginManager().registerListener(this, this);
        
        //Desactivado por OutOfMemoryError
        /*getProxy().getScheduler().schedule(this, () -> {
            sendUpdatedServerStatus();
        }, 5, 5, TimeUnit.SECONDS);*/
    }

    @EventHandler
    public void onMessageReceived(PluginMessageEvent e) {
        if (!e.getTag().equalsIgnoreCase(pluginChannel))
            return;
        
        ByteArrayDataInput in = ByteStreams.newDataInput(e.getData().clone());
        String subchannel = in.readUTF();
        if (subchannel.equals("bestLobby")) { 
            ProxiedPlayer p = getProxy().getPlayer(in.readUTF());
            ServerInfo lobby = getOneLobby();
            if (lobby.getAddress() != p.getServer().getInfo().getAddress()) {
                p.connect(getOneLobby());
            }
            return;
        } else if (subchannel.equals("askServerList")) { 
            sendUpdatedServerStatus();
            return;
        }
        
        //Reenviar los datos recibidos por el canal a todos los servidores
        getProxy().getServers().values().stream()
                .filter(server -> !server.getPlayers().isEmpty())
                .forEach(server ->  server.sendData(pluginChannel, e.getData()));
    }

    @EventHandler
    public void onProxyPing(ProxyPingEvent e) {
        ServerPing.Protocol version = e.getResponse().getVersion();
        version.setName("Undergames.es 1.10");
        version.setProtocol(210);
        e.getResponse().setVersion(version);
    }
    
    @EventHandler
    public void onPlayerJoin(PostLoginEvent e) {
        ProxiedPlayer p = e.getPlayer();
        p.connect(getOneLobby());
    }
        
    @EventHandler
    public void onServerKickEvent(ServerKickEvent e) {
        e.setCancelled(true);
        e.setCancelServer(getOneLobby());
    }
    
    private ServerInfo getOneLobby() {
        ServerInfo bestOption = getProxy().getServerInfo("lobby1");
        for (ServerInfo s : getProxy().getServers().values()) {
            if (s.getName().contains("lobby")) {
                if (s.getPlayers().size() < bestOption.getPlayers().size()) {
                    bestOption = s;
                }
            }
        }
        return bestOption;
    }
    
    public void sendUpdatedServerStatus() {
        if (getProxy().getPlayers().isEmpty()) return;
        
        //Serializar datos de servidores y enviar bytes
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("serversinfo");
        ArrayList<FEMServerInfo> lista = new ArrayList<>();
        getProxy().getServers().values().forEach(s -> {
            ArrayList<String> users = new ArrayList<>();
            s.getPlayers().forEach(pp -> users.add(pp.getUniqueId().toString()));
            FEMServerInfo server = new FEMServerInfo(s.getName(), s.getPlayers().size(), users);
            
            lista.add(server);
        });
        out.writeUTF(new Gson().toJson(lista));
        getProxy().getServers().values().forEach(s -> s.sendData(pluginChannel, out.toByteArray()));
    }
    
    @Data
    @AllArgsConstructor
    public class FEMServerInfo {
        private String name;
        private Integer players;
        private ArrayList<String> users;
    }
}