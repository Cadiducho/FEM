package com.cadiducho.fem.uhc.player;

import java.util.Arrays;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import com.cadiducho.fem.uhc.utils.ItemBuilder;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitScheduler;

public class UHCPlayer {

    public Main plugin;

    public UHCPlayer(Main plugin) {
        this.plugin = plugin;
    }
    
    int taskid;

    public void esperan2(Player player) {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        if (GameState.state == GameState.LOBBY) {
            ArrayList<String> A = new ArrayList<>();
            A.clear();
            A.add("1");
            taskid = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
                Integer i = A.size();
                if (i == 1) {
                    plugin.msg.sendActionBar(player, "&f&lDonkeyCraft &8| &c&lEsperando.");
                    A.add("A");
                }
                if (i == 2) {
                    plugin.msg.sendActionBar(player, "&6&lD&f&lonkeyCraft &8| &c&lEsperando..");
                    A.add("B");
                }
                if (i == 3) {
                    plugin.msg.sendActionBar(player, "&6&lDo&f&lnkeyCraft &8| &c&lEsperando...");
                    A.add("C");
                }
                if (i == 4) {
                    plugin.msg.sendActionBar(player, "&6&lDon&f&lkeyCraft &8| &c&lEsperando.");
                    A.add("D");
                }
                if (i == 5) {
                    plugin.msg.sendActionBar(player, "&6&lDonk&f&leyCraft &8| &c&lEsperando..");
                    A.add("E");
                }
                if (i == 6) {
                    plugin.msg.sendActionBar(player, "&6&lDonke&f&lyCraft &8| &c&lEsperando...");
                    A.add("F");
                }
                if (i == 7) {
                    plugin.msg.sendActionBar(player, "&6&lDonkeyC&f&lraft &8| &c&lEsperando.");
                    A.add("G");
                }
                if (i == 7) {
                    plugin.msg.sendActionBar(player, "&6&lDonkeyCr&f&laft &8| &c&lEsperando..");
                    A.add("H");
                }
                if (i == 8) {
                    plugin.msg.sendActionBar(player, "&f&lDonkeyCra&f&lft &8| &c&lEsperando...");
                    A.add("I");
                }
                if (i == 9) {
                    plugin.msg.sendActionBar(player, "&6&lDonkeyCraf&f&lt &8| &c&lEsperando.");
                    A.add("J");
                }
                if (i == 10) {
                    plugin.msg.sendActionBar(player, "&6&lDonkeyCraft &8| &c&lEsperando.");
                    A.add("K");
                }
                if (i == 11) {
                    plugin.msg.sendActionBar(player, "&f&lDonkeyCraft &8| &c&lEsperando..");
                    A.add("L");
                }
                if (i == 12) {
                    plugin.msg.sendActionBar(player, "&6&lDonkeyCraft &8| &c&lEsperando...");
                    A.add("M");
                }
                if (i == 12) {
                    if (GameState.state == GameState.TELETRANSPORTE) {
                        scheduler.cancelTask(taskid);
                        plugin.getServer().getScheduler().cancelTask(taskid);
                    } else {
                        A.clear();
                        A.add("1");
                    }
                }
            }, 0, 5);
        } else {
            scheduler.cancelTask(taskid);
            plugin.getServer().getScheduler().cancelTask(taskid);
        }
    }

    public void sendToServer(Player player, String targetServer) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(targetServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
    }

    public void sendFirework(Player p) {
        Random random = new Random();
        int r = random.nextInt(256), g = random.nextInt(256), b = random.nextInt(256);
        spawnFirework(p.getLocation(), plugin.wm.getArenaWorld(), Color.fromRGB(r, g, b));
    }

    public void spawnFirework(Location location, World world, Color color) {
        Firework firework = world.spawn(location, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder().withColor(color).with(FireworkEffect.Type.BALL).trail(true).build();
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);
    }

    public void setSpectator(Player player) {
        plugin.gm.addSpectator(player);
        setCleanPlayer(player, GameMode.SPECTATOR);
        plugin.msg.sendTitle(player, "&c&lERES UN ESPECTADOR", "", 5, 10, 5);
        plugin.gm.PLAYERS_IN_GAME.stream()
                .map(id -> plugin.getServer().getPlayer(id))
                .forEach(pl -> pl.hidePlayer(player));
    }

    public void setLobbyPlayer(Player player) {
        setCleanPlayer(player, GameMode.ADVENTURE);
        plugin.msg.sendTitle(player, "&c&lDonkey&b&lUHC", "&7¡Bienvenido &e" + player.getDisplayName() + "&7!", 5, 10, 5);
        plugin.msg.sendColorMessage(player, " ");
        plugin.msg.sendColorMessage(player, "         &c&lDonkey&b&lUHC         ");
        plugin.msg.sendColorMessage(player, "&7Bienvenido a servidor de &eUHC &7de &cDonkey&bCraft");
        plugin.msg.sendColorMessage(player, " ");
        ItemStack INFO = new ItemBuilder(Material.PAPER).setDisplayName("§a§lINFORMACIÓN").setLores(Arrays.asList("§7Información de UHC")).getItem();
        player.getInventory().setItem(0, INFO);
        ItemStack LOBBY = new ItemBuilder(Material.SLIME_BALL).setDisplayName("§c§lVOLVER AL LOBBY").getItem();
        player.getInventory().setItem(8, LOBBY);
        ItemStack VIP = new ItemBuilder(Material.DIAMOND).setDisplayName("§b§lMENÚ VIP").getItem();
        player.getInventory().setItem(4, VIP);
    }

    public void setCleanPlayer(Player player, GameMode gamemode) {
        player.setHealth(20d);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setTotalExperience(0);
        player.setGameMode(gamemode);
        player.setFireTicks(0);
        player.setExp(0f);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getActivePotionEffects().stream().forEach((effect) -> {
            player.removePotionEffect(effect.getType());
        });
    }

}
