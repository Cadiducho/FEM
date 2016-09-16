package com.cadiducho.fem.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class FEMBungee extends Plugin implements Listener {

    String pluginChannel = "FEM";

    @Override
    public void onEnable() {
        getProxy().registerChannel(pluginChannel);
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
        getProxy().getScheduler().schedule(this, () -> {
            sendUpdatedServerStatus();
        }, 5, 5, TimeUnit.SECONDS);
    }

    @EventHandler
    public void onMessageReceived(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase(pluginChannel))
            return;

        //Reenviar los datos recibidos por el canal a todos los servidores
        ProxyServer.getInstance().getServers().values().stream()
                .filter(server -> !server.getPlayers().isEmpty())
                .forEach(server ->  server.sendData(pluginChannel, event.getData()));
    }

    @EventHandler
    public void onProxyPing(ProxyPingEvent e) {
        ServerPing.Protocol version = e.getResponse().getVersion();
        version.setName("FEM 1.10");
        version.setProtocol(210);
        e.getResponse().setVersion(version);
    }
    
    public void sendUpdatedServerStatus() {
        //Serializar datos de servidores y enviar bytes
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("serversinfo");
        ArrayList<FEMServerInfo> lista = new ArrayList<>();
        for (ServerInfo s : ProxyServer.getInstance().getServers().values()) {
            ArrayList<String> users = new ArrayList<>();
            s.getPlayers().forEach(pp -> users.add(pp.getUniqueId().toString()));
            FEMServerInfo server = new FEMServerInfo(s.getName(), s.getPlayers().size(), users);
            lista.add(server);
            //out.writeUTF(new Gson().toJson(server));
        }
        out.writeUTF(new Gson().toJson(lista));
        ProxyServer.getInstance().getServers().values().forEach(s ->  s.sendData(pluginChannel, out.toByteArray()));
    }
    
    @Data
    @AllArgsConstructor
    public class FEMServerInfo {
        private String name;
        private Integer players;
        private ArrayList<String> users;
    }
}