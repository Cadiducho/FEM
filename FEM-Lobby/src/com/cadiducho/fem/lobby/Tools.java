package com.cadiducho.fem.lobby;

import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.Metodos;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class Tools {

    public static ItemStack navegator;
    public static ItemStack libro;
    public static ItemStack settings;

    /**
     * Más que nada cargo los ítems una vez y no los ando creando cada vez que
     * entran.
     */
    public static void init() {
        navegator = ItemUtil.createItem(Material.COMPASS, "&lJuegos", "Desplazate entre los juegos del servidor");
        libro = makeBook();
        settings = ItemUtil.createItem(Material.COMMAND, "&lAjustes", "Cambia alguno de tus ajustes de usuario");
    }

    private static ItemStack makeBook() {
        ItemStack guia = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) guia.getItemMeta();
        meta.setDisplayName(Metodos.colorizar("Guía del novato"));
        meta.setAuthor(Metodos.colorizar("&6Staff"));

        meta.addPage(Metodos.colorizar("    &lItroducción&r:\n"
                + " \n"
                + "UnderGames es un servidor de &2Mine&6craft&0 patrocinado por &aNVIDIA&0 en el cual se puede disfrutar de distintos minijuegos. Para saber más sobre estos minijuegos, pasa a la siguiente página."));
        meta.addPage(Metodos.colorizar("    &lServidores&r:\n"
                + " \n"
                + "-&aGemHunters\n"
                + "-&5DyeOrDie\n"
                + "-&1TntWars\n"
                + "-&6BattleRoyale\n"
                + "-&4LuckyGladiators\n"
                + "-&ePictograma&r\n"
                + " \n"
                + "En las siguientes páginas se encuentran sus descripciones."));
        meta.addPage(Metodos.colorizar("    &a&lGemHunters&r:\n"
                + " \n"
                + "Al comenzar el servidor te asignará un equipo. Durante 30 segundos tu objetivo será esconder la gema de tu inventario lo mejor posible. Tras ese tiempo, tu objetivo cambiará a encontrar las gemas del enemigo y defender las propias."));
        meta.addPage(Metodos.colorizar("    &5&lDyeOrDie&r:\n"
                + " \n"
                + "El objetivo será situarse sobre el color que haya sido seleccionado en la ronda, tras unos segundos toda la pista excepto el bloque con el color escogido caerá al vacío. Si caes al vacío perderás."));
        meta.addPage(Metodos.colorizar("      &1&lTntWars&r:\n"
                + " \n"
                + "El objetivo en este juego se basa en colocar una TNT en la torre del enemigo al mismo tiempo que defiendes tu torre. Cuando esta sea colocada en tu torre tendrás 5 segundos para quitarla o tu base explotará y no podrás reaparecer."));
        meta.addPage(Metodos.colorizar("     &6&lBattleRoyale&r:\n"
                + " \n"
                + "Serás teletransportado a un mapa en el cual se ecuentran escondidos infinidad de cofres con objetos para equiparse. El objetivo consistirá en ser el último jugador vivo en el mapa."));
        meta.addPage(Metodos.colorizar(" &4&lLuckyGladiators&r:\n"
                + " \n"
                + "Te econtarás ante LuckyBlocks los cuales te darán objetos aleatorios. Pasado un tiempo, dispondrás de una sala para encantar y/o craftear. Después, serás llevado a un coliseo para pelear hasta que solo sobreviva uno."));
        meta.addPage(Metodos.colorizar("    &e&lPictograma&r:\n"
                + " \n"
                + "El objetivo en este juego se basa en dibujar. Cada ronda será seleccionado un artista el cual intentará la palabra que se aleatoriamente escogida mientras los demás usuarios intentan adivinarla."));
        guia.setItemMeta(meta);
        return guia;
    }

}
