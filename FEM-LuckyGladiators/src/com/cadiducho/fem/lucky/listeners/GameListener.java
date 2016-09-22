package com.cadiducho.fem.lucky.listeners;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.lucky.LuckyGladiators;
import com.cadiducho.fem.lucky.manager.GameState;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;

public class GameListener implements Listener {

    private final LuckyGladiators plugin;

    public GameListener(LuckyGladiators instance) {
        plugin = instance;
    }

    @EventHandler
    public void onMotd(ServerListPingEvent e) {
        e.setMotd(GameState.getParsedStatus());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        if (plugin.getGm().isInGame()) {
            if (e.getEntity().getKiller() instanceof Player) {
                e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                plugin.getMsg().sendMessage(e.getEntity(), "Has matado a " + e.getEntity().getKiller().getDisplayName());
                plugin.getMsg().sendBroadcast(e.getEntity().getDisplayName() + " ha sido eliminado de la partida");
                plugin.getGm().getPlayersInGame().remove(e.getEntity());
                plugin.getPm().setSpectator(e.getEntity());
                FEMServer.getUser(e.getEntity()).sendMessage("Escribe &e/lobby &fpara volver al Lobby");
                FEMServer.getUser(e.getEntity()).repeatActionBar("Escribe &e/lobby &fpara volver al Lobby");
                plugin.getPm().addKillToPlayer(e.getEntity().getKiller());
                
                //Stats
                HashMap<Integer, Integer> kills = FEMServer.getUser(e.getEntity().getKiller()).getUserData().getKills();
                kills.replace(6, kills.get(6) + 1);
                FEMServer.getUser(e.getEntity().getKiller()).getUserData().setKills(kills);
                FEMServer.getUser(e.getEntity().getKiller()).save();
                HashMap<Integer, Integer> deaths = FEMServer.getUser(e.getEntity()).getUserData().getDeaths();
                deaths.replace(6, deaths.get(6) + 1);
                FEMServer.getUser(e.getEntity()).getUserData().setDeaths(deaths);
                FEMServer.getUser(e.getEntity()).save();
            } else {
                plugin.getMsg().sendMessage(e.getEntity(), "Has muerto");
                e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                plugin.getMsg().sendBroadcast(e.getEntity().getDisplayName() + " ha sido eliminado de la partida");
                plugin.getGm().getPlayersInGame().remove(e.getEntity());
                plugin.getPm().setSpectator(e.getEntity());
                FEMServer.getUser(e.getEntity()).sendMessage("Escribe &e/lobby &fpara volver al Lobby");
                FEMServer.getUser(e.getEntity()).repeatActionBar("Escribe &e/lobby &fpara volver al Lobby");
                HashMap<Integer, Integer> deaths = FEMServer.getUser(e.getEntity()).getUserData().getDeaths();
                deaths.replace(6, deaths.get(6) + 1);
                FEMServer.getUser(e.getEntity()).getUserData().setDeaths(deaths);
                FEMServer.getUser(e.getEntity()).save();
            }
            if (!plugin.getGm().checkWinner()) {
                plugin.getGm().checkDm();
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!plugin.getGm().isInGame()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!plugin.getGm().isInGame()) {
            e.setCancelled(true);
        }
        if (e.getEntity() instanceof Player) {
            if (GameState.state == GameState.LUCKY || GameState.state == GameState.CRAFT) {
                e.setDamage(0.0);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!plugin.getGm().isInGame()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (GameState.state == GameState.LUCKY) {
            Block block = e.getBlock();
            block.getDrops();
            Player p = e.getPlayer();
            Location loc = block.getLocation();
            World w = p.getWorld();
            if (block.getType() == Material.SPONGE) {
                e.getBlock().setType(Material.AIR);
                Random rand = new Random();
                int proporcion = rand.nextInt(100);
                ArrayList<Material> objetosNormales = Lists.newArrayList(Material.LOG, Material.WOOD, Material.STICK, Material.COAL, Material.STONE, Material.COBBLESTONE,
                        Material.GOLD_ORE, Material.IRON_ORE, Material.IRON_INGOT, Material.APPLE, Material.BEETROOT, Material.PORK, Material.POISONOUS_POTATO, Material.POTATO_ITEM, 
                        Material.CARROT_ITEM, Material.STRING, Material.FLINT, Material.SAND, Material.WOOL, Material.FLOWER_POT_ITEM, Material.COCOA, Material.SUGAR, Material.EGG,
                        Material.SULPHUR, Material.COOKIE, Material.INK_SACK, Material.FIREWORK, Material.DIRT, Material.MELON, Material.QUARTZ, Material.RAW_CHICKEN, Material.RAW_FISH,
                        Material.RAW_BEEF, Material.RABBIT, Material.PAPER, Material.MUTTON);
                ArrayList<Material> objetosPro = Lists.newArrayList(Material.IRON_INGOT, Material.COAL_BLOCK, Material.BEETROOT_SOUP, Material.GOLD_INGOT, Material.COOKED_MUTTON, 
                        Material.COOKED_RABBIT, Material.MUSHROOM_SOUP, Material.STONE, Material.STICK, Material.FISHING_ROD, Material.QUARTZ, Material.EMERALD);
                ArrayList<Material> objetosChetos = Lists.newArrayList(Material.DIAMOND, Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.ARROW, Material.ELYTRA);

                ArrayList<ItemStack> drops = Lists.newArrayList();
                for (int i = (rand.nextInt(4) + 1); i > 0; i--) { //Máximo 4 veces
                    if (proporcion >= 50) { //50%
                        drops.add(new ItemStack(objetosNormales.get(rand.nextInt(objetosNormales.size() - 1)), (rand.nextInt(4) + 1))); //de 1 a 5
                    } else if (proporcion >= 30) { //30%
                        drops.add(new ItemStack(objetosPro.get(rand.nextInt(objetosPro.size() - 1)), (rand.nextInt(2) + 1))); //de 1 a 3
                    } else if (proporcion > 15 && proporcion < 29) { //9%
                        drops.add(new ItemStack(objetosChetos.get(rand.nextInt(objetosChetos.size() - 1)), (rand.nextInt(1) + 1))); //de 1 a 2
                    } //15% de que no salga nada
                }

                drops.forEach(d -> w.dropItemNaturally(loc.add(0, 1.3, 0), d));
                FEMServer.getUser(p).getUserData().setLuckyRotos(FEMServer.getUser(p).getUserData().getLuckyRotos() + 1);
                FEMServer.getUser(p).save();
            }
            e.setCancelled(true);
        } else {
            e.setCancelled(true);
        }
    }
}
