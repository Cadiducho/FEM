package com.cadiducho.fem.lobby;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SignManager extends BukkitRunnable implements Listener {

    HashMap<Sign, ServerSign> signpool = new HashMap<>();
    Queue<ServerSign> gamequeue = new LinkedList<>();
    public static Lobby plugin;
    public static String[] signlines = {"--------", "Waiting", "", "--------"};
    private static SignManager signManager = null;

    public static SignManager getSignManager() {
        if (signManager == null) {
            signManager = new SignManager();
        }
        return signManager;
    }

    public void start() {
        FileConfiguration config = plugin.getConfig();
        if (!config.contains("signs.format.WaitingForNewGame")) {
            config.set("signs.format.WaitingForNewGame.lines.1", signlines[0]);
            config.set("signs.format.WaitingForNewGame.lines.2", signlines[1]);
            config.set("signs.format.WaitingForNewGame.lines.3", signlines[2]);
            config.set("signs.format.WaitingForNewGame.lines.4", signlines[3]);
            plugin.saveConfig();
        }
        signlines[0] = config.getString("signs.format.WaitingForNewGame.lines.1");
        signlines[1] = config.getString("signs.format.WaitingForNewGame.lines.2");
        signlines[2] = config.getString("signs.format.WaitingForNewGame.lines.3");
        signlines[3] = config.getString("signs.format.WaitingForNewGame.lines.4");
        for (Sign sign : this.signpool.keySet()) {
            formatEmptySign(sign);
        }
        runTaskTimer(plugin, 20L, 20L);
    }

    public boolean registerSign(Sign sign) {
        if (!this.signpool.containsKey(sign)) {
            this.signpool.put(sign, null);
            formatEmptySign(sign);
            return true;
        }
        return false;
    }

    public Sign getEmptySign() {
        for (Sign sign : this.signpool.keySet()) {
            if (this.signpool.get(sign) == null) {
                return sign;
            }
        }
        return null;
    }

    public ServerSign getBySign(Sign sign) {
        if (this.signpool.containsKey(sign)) {
            return (ServerSign) this.signpool.get(sign);
        }
        return null;
    }

    public void addToQueue(ServerSign instance) {
        if ((!this.gamequeue.contains(instance)) && (!this.signpool.containsValue(instance)) && (!this.signpool.values().contains(instance))) {
            this.gamequeue.add(instance);
        }
    }

    private void formatEmptySign(Sign sign) {
        sign.setLine(0, signlines[0].replaceAll("(&([a-f0-9]))", "�$2"));
        sign.setLine(1, signlines[1].replaceAll("(&([a-f0-9]))", "�$2"));
        sign.setLine(2, signlines[2].replaceAll("(&([a-f0-9]))", "�$2"));
        sign.setLine(3, signlines[3].replaceAll("(&([a-f0-9]))", "�$2"));
        sign.update();
    }

    @Override
    public void run() {
        for (Sign sign : this.signpool.keySet()) {
            if (this.signpool.get(sign) == null) {
                formatEmptySign(sign);
            } else {
                ServerSign instance = getBySign(sign);
                instance.updateSign(sign);
                if (!instance.needsPlayers()) {
                    this.signpool.put(sign, null);
                    formatEmptySign(sign);
                    sign.update();
                }
            }
        }
        while (!this.gamequeue.isEmpty()) {
            Sign emptysign = getEmptySign();
            if (emptysign == null) {
                break;
            }
            ServerSign instance = (ServerSign) this.gamequeue.poll();
            instance.updateSign(emptysign);
            emptysign.update();
            this.signpool.put(emptysign, instance);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoinAttempt(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && ((event.getClickedBlock().getState() instanceof Sign))) {
            ServerSign instance = getBySign((Sign) event.getClickedBlock().getState());
            if (instance != null) {
                System.out.print("Checking requirements to join");
                if ((instance.getGameState().equalsIgnoreCase("ENDING")) || (instance.getGameState().equalsIgnoreCase("RESTARTING"))) {
                    return;
                }
                if (instance.getMaxPlayers() <= instance.getPlayers()) {
                    if ((event.getPlayer().hasPermission("minigames.vip")) || (event.getPlayer().hasPermission("minigames.fullgames"))) {
                        instance.joinAttempt(event.getPlayer());
                        System.out.print(event.getPlayer().getName() + " joined  " + instance.getServerName() + ".");
                        return;
                    }
                }
                instance.joinAttempt(event.getPlayer());
                System.out.print(event.getPlayer().getName() + " joined  " + instance.getServerName() + ".");
                return;
            }
        }
    }
}
