package com.cadiducho.fem.lobby;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerManager {

    private final Lobby plugin;

    public PlayerManager(Lobby plugin) {
        this.plugin = plugin;
    }

    private FEMPlayercito fpc;

    public void setLobbyPlayer(Player player) {
        fpc = new FEMPlayercito(player);
        fpc.setLobbyPlayer();
    }

    /*
       Creo un player para no estar accediente cada vez al player de bukkit,
       y meter todo aquí y ya está.
     */
    private class FEMPlayercito {

        private final Player player;

        public FEMPlayercito(Player player) {
            this.player = player;
        }

        public void setLobbyPlayer() {
            cleanPlayer();
            player.getInventory().setItem(0, Tools.navegator);
            player.getInventory().setItem(4, Tools.libro);
            player.getInventory().setItem(8, Tools.settings);
        }

        /*
            El metodo que toda mujer quisiera tener.
         */
        public void cleanPlayer() {
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setExp(0);
            player.setLevel(0);
            player.setTotalExperience(0);
            player.setFireTicks(0);
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.setGameMode(GameMode.ADVENTURE);
            player.getActivePotionEffects().stream().forEach((effect) -> {
                player.removePotionEffect(effect.getType());
            });
        }

    }

}
