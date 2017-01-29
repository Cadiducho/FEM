package com.cadiducho.fem.core.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class Title {

    public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        clearTitle(player);
        try {
            Object e;
            Object chatTitle;
            Object chatSubtitle;
            Constructor subtitleConstructor;
            Object titlePacket;
            Object subtitlePacket;

            if (title != null) {
                title = ChatColor.translateAlternateColorCodes('&', title);
                title = title.replaceAll("%player%", player.getDisplayName());
                // Times packets
                e = ReflectionAPI.getNmsClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get((Object) null);
                chatTitle = ReflectionAPI.getNmsClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke((Object) null, new Object[]{"{\"text\":\"" + title + "\"}"});
                subtitleConstructor = ReflectionAPI.getNmsClass("PacketPlayOutTitle").getConstructor(new Class[]{ReflectionAPI.getNmsClass("PacketPlayOutTitle").getDeclaredClasses()[0], ReflectionAPI.getNmsClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE});
                titlePacket = subtitleConstructor.newInstance(new Object[]{e, chatTitle, fadeIn, stay, fadeOut});
                ReflectionAPI.sendPacket(player, titlePacket);

                e = ReflectionAPI.getNmsClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get((Object) null);
                chatTitle = ReflectionAPI.getNmsClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke((Object) null, new Object[]{"{\"text\":\"" + title + "\"}"});
                subtitleConstructor = ReflectionAPI.getNmsClass("PacketPlayOutTitle").getConstructor(new Class[]{ReflectionAPI.getNmsClass("PacketPlayOutTitle").getDeclaredClasses()[0], ReflectionAPI.getNmsClass("IChatBaseComponent")});
                titlePacket = subtitleConstructor.newInstance(new Object[]{e, chatTitle});
                ReflectionAPI.sendPacket(player, titlePacket);
            }

            if (subtitle != null) {
                subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
                subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
                // Times packets
                e = ReflectionAPI.getNmsClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get((Object) null);
                chatSubtitle = ReflectionAPI.getNmsClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke((Object) null, new Object[]{"{\"text\":\"" + title + "\"}"});
                subtitleConstructor = ReflectionAPI.getNmsClass("PacketPlayOutTitle").getConstructor(new Class[]{ReflectionAPI.getNmsClass("PacketPlayOutTitle").getDeclaredClasses()[0], ReflectionAPI.getNmsClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE});
                subtitlePacket = subtitleConstructor.newInstance(new Object[]{e, chatSubtitle, fadeIn, stay, fadeOut});
                ReflectionAPI.sendPacket(player, subtitlePacket);

                e = ReflectionAPI.getNmsClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get((Object) null);
                chatSubtitle = ReflectionAPI.getNmsClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke((Object) null, new Object[]{"{\"text\":\"" + subtitle + "\"}"});
                subtitleConstructor = ReflectionAPI.getNmsClass("PacketPlayOutTitle").getConstructor(new Class[]{ReflectionAPI.getNmsClass("PacketPlayOutTitle").getDeclaredClasses()[0], ReflectionAPI.getNmsClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE});
                subtitlePacket = subtitleConstructor.newInstance(new Object[]{e, chatSubtitle, fadeIn, stay, fadeOut});
                ReflectionAPI.sendPacket(player, subtitlePacket);
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }
    }

    public static void clearTitle(Player player) {
        sendTitle(player, 0, 0, 0, "", "");
    }
}
