package com.cadiducho.fem.core.listeners;

import com.cadiducho.fem.core.FEMCore;
import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.core.util.Metodos;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeListener implements PluginMessageListener {

    public static FEMCore plugin;

    public BungeeListener(FEMCore instance) {
        plugin = instance;
    }

    //Adminchat, HelpOp y más:
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
	if(!channel.equalsIgnoreCase("FEM")) return;
	DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(message));
	try {
            String subChannel = inputStream.readUTF();
            if (subChannel.equalsIgnoreCase("AdminChat")) {
		sendStaffMessage(inputStream.readUTF(), inputStream.readUTF(), inputStream.readUTF());
            }
            if (subChannel.equalsIgnoreCase("HelpOp")) {
                sendHelpOp(inputStream.readUTF(), inputStream.readUTF());
            }
            if(subChannel.equalsIgnoreCase("BroadCast")){
                sendBroadCast(inputStream.readUTF());
            }
	} catch(IOException e) {
            plugin.debugLog("Error enviando un mensaje: ");
            plugin.debugLog(e.getMessage());
	}
    }
    
    public void sendStaffMessage(String prefijo, String senderName, String mensaje) {
        plugin.getServer().getOnlinePlayers().stream()
            .filter(p -> FEMServer.getUser(p).isOnRank(FEMCmd.Grupo.Admin))
            .forEach(p -> {
                String msg = Metodos.colorizar(prefijo+" &b{0}&f: &a{1}"
                .replace("{0}", senderName).replace("{1}", mensaje));
                p.sendMessage(msg);
            }
        );
	plugin.log("[Staff Chat]" + senderName + " » " + mensaje);
    }
    
    public void sendHelpOp(String senderName, String mensaje) {
        plugin.getServer().getOnlinePlayers().stream()
            .filter(p -> FEMServer.getUser(p).isOnRank(FEMCmd.Grupo.Admin))
            .forEach(p -> {
                String msg = Metodos.colorizar(
                        plugin.getConfig().getString("helpop.sintaxis")
                        .replace("{0}", senderName).replace("{1}", mensaje));
                p.sendMessage(msg);
            }
        );
	plugin.log("[HelpOP]" + senderName + " » " + mensaje);
    }
    
    public void sendBroadCast(String mensaje) {
        plugin.getServer().getOnlinePlayers().stream()
            .forEach(p -> {
                String msg = Metodos.colorizar(
                        plugin.getConfig().getString("broadcast.sintaxis")
                                .replace("{1}", mensaje));
                p.sendMessage(msg);
            }
        );
	plugin.log("[Broadcast]" + " » " + mensaje);
    }
}
