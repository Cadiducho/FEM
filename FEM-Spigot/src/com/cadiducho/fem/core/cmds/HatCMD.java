package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;

public class HatCMD extends FEMCmd {

    public HatCMD(){
        super("sombrero", Grupo.Vip, Arrays.asList("hat"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args){
        if(args.length > 0 && (args[0].contains("quitar"))){

            PlayerInventory inv = user.getPlayer().getInventory();
            ItemStack enmano = inv.getHelmet();

            if(enmano == null || enmano.getType() == Material.AIR){
                user.sendMessage("*hat.error");
            } else {
                ItemStack aire = new ItemStack(Material.AIR);

                inv.setHelmet(aire);
                inv.addItem(enmano);
            }
        } else {
            if(user.getPlayer().getInventory().getItemInHand().getType() != Material.AIR){
                ItemStack mano = user.getPlayer().getInventory().getItemInHand();
                if(mano.getType().getMaxDurability() == 0){

                    PlayerInventory inv = user.getPlayer().getInventory();
                    ItemStack casco = inv.getHelmet();

                    inv.setHelmet(mano);
                    inv.setItemInHand(casco);
                    user.sendMessage("*hat.mensaje");
                }
                else {
                    user.sendMessage("*hat.error");
                }
            } else {
                user.sendMessage("*hat.error");
            }
        }
    }

}
