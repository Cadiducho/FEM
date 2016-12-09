package com.cadiducho.fem.lobby;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.Metodos;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LobbyMenu {

    private static Lobby plugin;
    public enum Menu {
        AJUSTES, VIAJAR, STATS, NVIDIA, PARTICULAS, PETS, CAJAS;
    }

    @Getter private static ItemStack libro;

    @Getter private static Inventory invAjustes;
    @Getter private static Inventory invViajar;
    @Getter private static Inventory invStats;
    @Getter private static Inventory invNvidia;
    @Getter private static Inventory invParticulas;
    @Getter private static Inventory invPets;
    @Getter private static Inventory invCajas;
    
    @Getter private static final List<String> loreAmistades = Arrays.asList("Informar de nuevos seguimientos de amistad", "- Informar (Verde)", "- No informar (Rojo)");
    @Getter private static final List<String> loreMsgPrivados = Arrays.asList("Acepta o rechaza los mensajes privados en el chat", "- Aceptar (Verde)", "- Rechaza (Rojo)");
    @Getter private static final List<String> loreVisibilidad = Arrays.asList("Selecciona que tipo de jugadores quieres ver", "- Todos (Verde)", "- Amigos (Amarillo)", "- Nadie (Rojo)");

    public LobbyMenu(Lobby instance) {
        plugin = instance;

        //Libro
        libro = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) libro.getItemMeta();
        meta.setDisplayName(Metodos.colorizar("Guía del novato"));
        meta.setAuthor(Metodos.colorizar("&6Staff"));
        meta.addPage(Metodos.colorizar("§7§l■ ¡BIENVENIDO/A! ■§0\n\n"
                + "§lUnderGames§r es un servidor apoyado por §a§lNVIDIA§0, en el cual puedes disfrutar de muchos minijuegos con tus amigos."
                + "Para saber más sobre estos minijuegos, primero lee las normas.§0"));
        meta.addPage(Metodos.colorizar("§7§l■ NORMAS: ■ §0\n\n"
                + "❶ No insultes.\n"
                + "❷ No hagas spam.\n"
                + "❸ No escribas abusivamente en el chat.\n"
                + "❹ No uses hacks/mods §o(excepto Optifine).§r\n"
                + "❺ No uses bugs, repórtalos!§0"));
        meta.addPage(Metodos.colorizar("§3§l▶ PICTOGRAMA:§0\n\n"
                + "En cada ronda será seleccionado un artista, el cual intentará representar la palabra oculta, mientras los demás usuarios intentan adivinarla. "
                + "El jugador con más puntos, ¡gana!§0"));
        meta.addPage(Metodos.colorizar("§1§l▶ TNT WARS:§0\n\n"
                + "Compite contra el resto de jugadores comprando armas, defensas y bloques."
                + "Pero ten cuidado, si un jugador hace estallar tu isla colocando la TNT, no podrás revivir. ¡Ganará el último jugador vivo!"));
        meta.addPage(Metodos.colorizar("§5§l▶ DYE or DIE:§0\n\n"
                + "Sitúate sobre el color que haya sido seleccionado en la ronda, tras unos segundos toda la pista caerá excepto el color correcto. "
                + "Sobrevive todas las rondas posibles. ¡Si caes al vacío perderás!§0"));
        meta.addPage(Metodos.colorizar("§4§l▶ LUCKY WARRIOR:§0\n\n"
                + "Rompe los §oluckyblocks.§r Pasado un tiempo, podrás cocinar, craftear, encantar y equiparte con lo obtenido. "
                + "Una vez listos, apareceréis en la arena, donde solo uno de vosotros vencerá.§0"));
        meta.addPage(Metodos.colorizar("§a§l▶ GEM HUNTERS:§0\n\n"
                + "Dos equipos deberán esconder sus gemas por el mapa. "
                + "Tras ese tiempo, el muro divisorio se abrirá y tu objetivo será encontrar las gemas enemigas y defender las propias, atacando al equipo contrario.§0"));
        meta.addPage(Metodos.colorizar("§6§l▶ BATTLE ROYALE:§0\n\n"
                + "Busca los cofres por el mapa, Equípate, compra o vende lo que necesites en las tiendas y se el último jugador con vida. "
                + "Recuerda que los limites del mapa disminuyen y te pueden eliminar.§0"));
        meta.addPage(Metodos.colorizar("Síguenos en Twitter:\n"
                + "&8&l@UnderGames_info\n"
                + " \n"
                + " \n"
                + "¡Bienvenido al servidor y disfruta! ;)"));
        libro.setItemMeta(meta);

        //Ajustes
        invAjustes = plugin.getServer().createInventory(null, 18, "Ajustes del jugador");
        invAjustes.setItem(1, ItemUtil.createHeadPlayer("NUEVAS AMISTADES", "steve", loreAmistades));
        invAjustes.setItem(4, ItemUtil.createItem(Material.BOOK_AND_QUILL, "MENSAJES PRIVADOS", loreMsgPrivados));
        invAjustes.setItem(7, ItemUtil.createItem(Material.ENDER_PEARL, "VISIBILIDAD DE JUGADORES", loreVisibilidad));

        //Viajar
        invViajar = plugin.getServer().createInventory(null, 27, "Viajar");
        invViajar.setItem(18, ItemUtil.createItem(Material.BEACON, "Lobbies"));
        invViajar.setItem(3, ItemUtil.createItem(Material.PAINTING, "&3&lPICTOGRAMA"));
        invViajar.setItem(4, ItemUtil.createItem(Material.TNT, "&1&lTNT WARS"));
        ItemStack letherBoots = ItemUtil.createItem(Material.LEATHER_BOOTS, "&5&lDYE OR DIE");
        LeatherArmorMeta lam = (LeatherArmorMeta) letherBoots.getItemMeta();
        lam.setColor(Color.BLUE);
        lam.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE);
        letherBoots.setItemMeta(lam);
        invViajar.setItem(5, letherBoots);
        invViajar.setItem(12, ItemUtil.createItem(Material.SKULL_ITEM, "&4&lLUCKY WARRIOR"));
        invViajar.setItem(13, ItemUtil.createItem(Material.EMERALD, "&a&lGEM HUNTERS"));
        invViajar.setItem(14, ItemUtil.createItem(Material.GOLD_SWORD, "&6&lBATTLE ROYALE"));
        
        //Stats
        invStats = plugin.getServer().createInventory(null, 18, "Estadisticas del jugador");

        //TODO: Añadir Lore

        //Nvidia
        invNvidia = plugin.getServer().createInventory(null, 36, Metodos.colorizar("&3NVIDIA &0Point"));
        invNvidia.setItem(11, ItemUtil.createItem(Material.INK_SACK, 1, (short)3, "Partículas", new ArrayList<>()));
        invNvidia.setItem(13, ItemUtil.createItem(Material.BONE, "Animales", new ArrayList<>()));
        invNvidia.setItem(15, ItemUtil.createItem(Material.IRON_SWORD, "Comprar Mejoras", new ArrayList<>()));
        invNvidia.setItem(31, ItemUtil.createItem(Material.ENCHANTMENT_TABLE, "Abrir Cajas", new ArrayList<>()));

        //Particulas
        invParticulas = plugin.getServer().createInventory(null, 36, "Particulas");
        invParticulas.setItem(0, ItemUtil.createItem(Material.FIRE, 1, "Espiral de Fuego", new ArrayList<>()));
        invParticulas.setItem(1, ItemUtil.createItem(Material.STATIONARY_WATER, 1, "Circulo de Agua", new ArrayList<>()));
    }

    public static void openMenu(FEMUser u, Menu type) {
        u.getPlayer().closeInventory();
        Inventory clon = null;
        switch (type) {
            case AJUSTES:
                clon = invAjustes;

                //Amistades
                DyeColor amistadesColor = u.getUserData().getFriendRequest() ? DyeColor.LIME : DyeColor.RED;
                clon.setItem(10, ItemUtil.createClay("NUEVAS AMISTADES", loreAmistades, amistadesColor));

                //Mensajes
                DyeColor mensajesColor = u.getUserData().getEnableTell() ? DyeColor.LIME : DyeColor.RED;
                clon.setItem(13, ItemUtil.createClay("MENSAJES PRIVADOS", loreMsgPrivados, mensajesColor));

                //Visibilidad
                DyeColor visibilidadColor = (u.getUserData().getHideMode() == 0 ? DyeColor.RED : (u.getUserData().getHideMode() == 1 ? DyeColor.YELLOW : DyeColor.LIME));
                clon.setItem(16, ItemUtil.createClay("VISIBILIDAD DE JUGADORES", loreVisibilidad, visibilidadColor));
                break;
            case VIAJAR:
                clon = invViajar;

                //Viajar - Stats
                String amistades = u.getUserData().getFriendRequest() ? "Aceptas" : "No aceptas";
                String otros = u.getUserData().getHideMode() == 0 ? "Nadie" : (u.getUserData().getHideMode() == 1 ? "Amigos" : "Todos");
                long secs = (u.getUserData().getTimePlayed() / 1000) % 60;
                long mins = (u.getUserData().getTimePlayed() / (1000 * 60)) % 60;
                long horas = (u.getUserData().getTimePlayed() / (1000 * 60 * 60)) % 24;

                clon.setItem(26, ItemUtil.createHeadPlayer("Información", u.getPlayer().getName(), Arrays.asList("Pulsa para ver estadísticas",
                        "Tiempo jugado: " + horas + " horas, " + mins + " minutos y " + secs + " segundos",
                        "Amistades: " + amistades,
                        "Ver a: " + otros)));
                clon.setItem(22, ItemUtil.createItem(Material.DOUBLE_PLANT, "Dinero", u.getUserData().getCoins() + " monedas"));

                break;
            case STATS:
                clon = invStats;

                clon.setItem(3, ItemUtil.createItem(Material.PAINTING, "&3&lPICTOGRAMA",
                        Arrays.asList("&fPartidas Jugadas: &l" + u.getUserData().getPlays().get(4),
                                "&fPartidas Ganadas: &l" + u.getUserData().getWins().get(4),
                                "&e---{*}---",
                                "&fPalabras acertadas: &l" + u.getUserData().getPicAcertadas(),
                                "&fPalabras bien dibujadas: &l" + u.getUserData().getPicDibujadas())));
                clon.setItem(4, ItemUtil.createItem(Material.TNT, "&1&lTNT WARS",
                        Arrays.asList("&fPartidas Jugadas: &l" + u.getUserData().getPlays().get(1),
                                "&fPartidas Ganadas: &l" + u.getUserData().getWins().get(1),
                                "&fBajas: &l" + u.getUserData().getKills().get(1), "&fMuertes: &l" + u.getUserData().getDeaths().get(1),
                                "&1---{*}---",
                                "&fTNT Activadas: &l" + u.getUserData().getTntPuestas(), "&fTNT Desactivadas: &l" + u.getUserData().getTntQuitadas(),
                                "&fTNT Explotadas: &f" + u.getUserData().getTntExplotadas(), "&fGeneradores mejorados: &l" + u.getUserData().getGenUpgraded())));
                ItemStack letherBoots = ItemUtil.createItem(Material.LEATHER_BOOTS, "&5&lDYE OR DIE", Arrays.asList("&fPartidas Jugadas: &l" + u.getUserData().getPlays().get(1),
                        "&fPartidas Ganadas: &l" + u.getUserData().getWins().get(1),
                        "&5---{*}---",
                        "&fRecord de ronda: &l" + u.getUserData().getRecord_dod(),
                        "&fRondas jugadas: &l" + u.getUserData().getRondas_dod()));
                LeatherArmorMeta lam = (LeatherArmorMeta) letherBoots.getItemMeta();
                lam.setColor(Color.BLUE);
                letherBoots.setItemMeta(lam);
                clon.setItem(5, letherBoots);
                clon.setItem(12, ItemUtil.createItem(Material.SKULL_ITEM, "&4&lLUCKY WARRIOR", Arrays.asList("&fPartidas Jugadas: &l" + u.getUserData().getPlays().get(6),
                        "&fPartidas Ganadas: &l" + u.getUserData().getWins().get(6),
                        "&fBajas: &l" + u.getUserData().getKills().get(6), "&fMuertes: &l" + u.getUserData().getDeaths().get(6),
                        "&4---{*}---",
                        "&fLuckies rotos: &l" + u.getUserData().getLuckyRotos())));
                clon.setItem(13, ItemUtil.createItem(Material.EMERALD, "&a&lGEM HUNTERS",
                        Arrays.asList("&fPartidas Jugadas: &l" + u.getUserData().getPlays().get(3),
                                "&fPartidas Ganadas: &l" + u.getUserData().getWins().get(3),
                                "&fBajas: &l" + u.getUserData().getKills().get(3), "&fMuertes: &l" + u.getUserData().getDeaths().get(3),
                                "&a---{*}---",
                                "&fGemas escondidas: &l" + u.getUserData().getGemPlanted(), "&fGemas destruidas: &l" + u.getUserData().getGemDestroyed())));
                clon.setItem(14, ItemUtil.createItem(Material.GOLD_SWORD, "&6&lBATTLE ROYALE", Arrays.asList("&fPartidas Jugadas: &l" + u.getUserData().getPlays().get(5),
                        "&fPartidas Ganadas: &l" + u.getUserData().getWins().get(5),
                        "&fBajas: &l" + u.getUserData().getKills().get(5), "&fMuertes: &l" + u.getUserData().getDeaths().get(5),
                        "&6---{*}---",
                        "&fIntercambios realizados: &l" + u.getUserData().getBrIntercambios())));
                break;
            case NVIDIA:
                clon = invNvidia;
        }
        
        if (clon != null) {
            u.getPlayer().closeInventory();
            u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
            u.getPlayer().openInventory(clon);
        }
    }
}
