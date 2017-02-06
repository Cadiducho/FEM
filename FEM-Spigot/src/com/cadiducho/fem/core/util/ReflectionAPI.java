package com.cadiducho.fem.core.util;

import com.cadiducho.fem.core.FEMCore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Level;

public class ReflectionAPI {

    private static HashMap<Class<? extends Entity>, Method> handles = new HashMap<Class<? extends Entity>, Method>();
    private static Field player_connection = null;
    private static Method player_sendPacket = null;

    public static String getVersion() {
        String[] array = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",");
        if (array.length == 4)
            return array[3] + ".";
        return "";
    }

    public static int getPlayerVersion(Player p) {
        try {
            Class craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + getVersion() + ".entity.CraftPlayer");
            Class entityPlayerClass = Class.forName("net.minecraft.server." + getVersion() + ".EntityPlayer");
            Class playerConnectionClass = Class.forName("net.minecraft.server." + getVersion() + ".PlayerConnection");
            Class networkManagerClass = Class.forName("net.minecraft.server." + getVersion() + ".NetworkManager");

            Method method = craftPlayerClass.getDeclaredMethod("getHandle");
            method.setAccessible(true);
            Object entityPlayer = method.invoke(p);

            Field player = entityPlayerClass.getDeclaredField("playerConnection");
            player.setAccessible(true);
            Object playerConnection = player.get(entityPlayer);

            Field network = playerConnectionClass.getDeclaredField("networkManager");
            network.setAccessible(true);
            Object networkManager = network.get(playerConnection);

            Method version = networkManagerClass.getDeclaredMethod("getVersion");
            method.setAccessible(true);
            return (int) version.invoke(networkManager);

        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException e) {
            FEMCore.getInstance().getLogger().log(Level.INFO, "Error al sacar la version del jugador");
        }
        return -1;
    }

    public static Class<?> getNmsClass(String name) {
        String version = getVersion();
        String className = "net.minecraft.server." + version + name;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return clazz;
    }

    public static Field getFirstFieldByType(Class<?> clazz, Class<?> type) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType() == type) {
                return field;
            }
        }
        return null;
    }

    public static Field getField(Class<?> cl, String field_name) {
        try {
            return cl.getDeclaredField(field_name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getHandle(Entity entity) {
        try {
            return getMethod(entity.getClass(), "getHandle").invoke(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getHandle(World world) {
        try {
            return getMethod(world.getClass(), "getHandle").invoke(world);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Method getMethod(Class<?> cl, String method, Class<?>... args) {
        for (Method m : cl.getMethods())
            if (m.getName().equals(method) && ClassListEqual(args, m.getParameterTypes()))
                return m;
        return null;
    }

    public static Method getMethod(Class<?> cl, String method) {
        for (Method m : cl.getMethods())
            if (m.getName().equals(method))
                return m;
        return null;
    }

    public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;
        if (l1.length != l2.length)
            return false;
        for (int i = 0; i < l1.length; i++)
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        return equal;
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object nmsPlayer = getHandle(player);
            Field connectionField = nmsPlayer.getClass().getField("playerConnection");
            Object connection = connectionField.get(nmsPlayer);
            Method sendPacket = getMethod(connection.getClass(), "sendPacket");
            sendPacket.invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
