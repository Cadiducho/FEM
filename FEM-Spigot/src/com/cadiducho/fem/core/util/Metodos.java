package com.cadiducho.fem.core.util;

import com.cadiducho.fem.core.FEMCore;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Metodos {
    
    public static FEMCore plugin;
    
    public Metodos(FEMCore instance) {
	plugin = instance;
    }
    
    /*
     * Metodos generales
     */
    public static String colorizar(String s) {
        if (s == null) return "";
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String buildString(String[] args) {
        return buildString(args, 0);
    }
    
    public static String buildString(String[] args, int empiece) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = empiece; i < args.length; i++) {
            if (i > empiece) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(args[i]);
        }
        return stringBuilder.toString();
    }
    
    public static String readUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection request1 = (HttpURLConnection) url.openConnection();
        request1.setRequestMethod("GET");
        request1.connect();
        InputStream is = request1.getInputStream();
        BufferedReader bf_reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = "";
        try {
            while ((line = bf_reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }
    
    public void enviarPorAdminChat(String nombre, String mensaje) {
	try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            
            String prefijo = "&7[&6Staff&7]";
            out.writeUTF("AdminChat");
            out.writeUTF(prefijo);
            out.writeUTF(nombre);
            out.writeUTF(mensaje);
            
            Collection<? extends Player> c = Bukkit.getOnlinePlayers(); //Usar jugador falso
            Player p = (Player) c.toArray()[0];
            p.sendPluginMessage(plugin, "FEM", b.toByteArray());
	} catch(IOException e) {
            plugin.debugLog("Error enviando un mensaje: ");
            plugin.debugLog(e.getMessage());
	}
    }
    
    public static void enviarPorHP(String nombre, String mensaje) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            
            out.writeUTF("HelpOp");
            out.writeUTF(nombre);
            out.writeUTF(mensaje);
            
            Collection<? extends Player> c = Bukkit.getOnlinePlayers(); //Usar jugador falso
            Player p = (Player) c.toArray()[0];
            p.sendPluginMessage(plugin, "FEM", b.toByteArray());
        } catch(IOException e) {
            plugin.debugLog("Error enviando un mensaje: ");
            plugin.debugLog(e.getMessage());
        }
    }
    
    public static void broadcastMsg(String mensaje) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            
            out.writeUTF("BroadCast");
            out.writeUTF(mensaje);
            
            Collection<? extends Player> c = Bukkit.getOnlinePlayers();
            Player p = (Player) c.toArray()[0];
            p.sendPluginMessage(plugin, "FEM", b.toByteArray());
        } catch(IOException e) {
            plugin.debugLog("Error enviando un mensaje: ");
            plugin.debugLog(e.getMessage());
        }
    }
    
    public static String locationToString(Location loc) {
        return loc.getWorld().getName() + "%" + loc.getX() + "%" + loc.getY() + "%" + loc.getZ() + "%" + loc.getYaw() + "%" + loc.getPitch();
    }
	
    public static Location stringToLocation(String string) {
        if (string == null) return null;
        String[] s = string.split("%");
	Location loc = new Location(Bukkit.getWorld(s[0]), Double.parseDouble(s[1]),
                Double.parseDouble(s[2]), Double.parseDouble(s[3]), Float.parseFloat(s[4]), Float.parseFloat(s[5]));
	return loc;
    }
    
    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Class<?> getBukkitClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}
