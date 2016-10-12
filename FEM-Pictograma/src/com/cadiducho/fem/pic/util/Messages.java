package com.cadiducho.fem.pic.util;

import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.pic.Pictograma;
import java.lang.reflect.Field;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Messages {

    private final Pictograma plugin;
    private final String prefix;

    public Messages(Pictograma instance, String pr) {
        plugin = instance;
        prefix = Metodos.colorizar(pr);
    }

    public void sendMessage(Player player, String msg) {
        player.sendMessage(Metodos.colorizar(prefix + " " + msg));
    }
    
    public void sendEmptyLine(){
        plugin.getServer().broadcastMessage("");
    }
    
    public void sendBroadcast(String msg){
        plugin.getServer().broadcastMessage(Metodos.colorizar(prefix + " " + msg));
    }

    public void sendActionBar(Player p, String msg) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer
                .a("{\"text\": \"" + Metodos.colorizar(msg) + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
    }

    public void sendHeaderAndFooter(Player p, String head, String foot) {
        CraftPlayer craftplayer = (CraftPlayer) p;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent header = IChatBaseComponent.ChatSerializer
                .a("{'color': '', 'text': '" + Metodos.colorizar(head) + "'}");
        IChatBaseComponent footer = IChatBaseComponent.ChatSerializer
                .a("{'color': '', 'text': '" + Metodos.colorizar(foot) + "'}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, header);
            headerField.setAccessible(!headerField.isAccessible());

            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, footer);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
        }
        connection.sendPacket(packet);
    }
}
