package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class BookCMD extends FEMCmd {

    public BookCMD() {
        super("book", Grupo.Owner, Arrays.asList("libro"));
    }

    @Override
    public void run(FEMUser user, String label, String[] args) {
        final ItemStack item = user.getPlayer().getInventory().getItemInMainHand();
        switch (item.getType()) {
            case WRITTEN_BOOK:
                BookMeta bmeta = (BookMeta) item.getItemMeta();
                ItemStack newItem = new ItemStack(Material.BOOK_AND_QUILL, item.getAmount());
                newItem.setItemMeta(bmeta);
                user.getPlayer().getInventory().setItemInMainHand(newItem);
                user.sendMessage("*book.desbloqueado");
                break;
            case BOOK_AND_QUILL:
                BookMeta bmeta2 = (BookMeta) item.getItemMeta();
                bmeta2.setAuthor(user.getName());
                ItemStack newItem2 = new ItemStack(Material.WRITTEN_BOOK, item.getAmount());
                newItem2.setItemMeta(bmeta2);
                user.getPlayer().getInventory().setItemInMainHand(newItem2);
                user.sendMessage("*book.bloqueado");
                break;
            default:
                user.sendMessage("*book.uso");
                break;
        }
    }
}
