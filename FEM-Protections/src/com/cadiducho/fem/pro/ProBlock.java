package com.cadiducho.fem.pro;

import com.cadiducho.fem.core.util.Metodos;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;

import java.util.*;

public class ProBlock {

    @Getter private int id;
    @Getter private Block block;
    @Getter private ProPlayer proPlayer;

    private static final Protections pro = Protections.getInstance();

    public ProBlock(Block b, ProPlayer proPlayer){
        this.block = b;
        this.proPlayer = proPlayer;
    }

    public ProBlock(ProPlayer proPlayer){
        this.proPlayer = proPlayer;
    }

    public ProBlock(int id){
        this.id = id;
    }

    public ProBlock(Block b){
        this.block = b;
    }

    public void protectBlock(){
        this.id = pro.getFiles().getID("blocks");

        pro.getFiles().getBlocks().set("block_" + id + ".loc", Metodos.locationToString(block.getLocation()));
        pro.getFiles().getBlocks().set("block_" + id + ".admins", Arrays.asList(proPlayer.getUuid()));
        pro.getFiles().getBlocks().set("block_" + id + ".users", Arrays.asList(""));
        setDefaultFlags();
        pro.getFiles().saveFiles();

        proPlayer.getPlayer().playSound(proPlayer.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        proPlayer.sendMessage("Bloque Protegido");

    }

    public void removeProtection(){
        pro.getFiles().getBlocks().set("block_" + id, null);
        pro.getFiles().saveFiles();
    }


    public Location getLocation(){
        return Metodos.stringToLocation(pro.getFiles().getAreas().getString("block_" + id + ".loc"));
    }

    //Utils
    public boolean isProtected() {
        return pro.getFiles().getBlocks().contains("block_" + getBlockID());
    }

    public boolean exist(){
        return pro.getFiles().getBlocks().contains("block_" + id);
    }

    //Getters
    public List<ProPlayer> getProtectionPlayers(){
        List<ProPlayer> players = new ArrayList<>();

        for (int x = 0; x < pro.getFiles().getCurrentID("blocks"); x++){
            if (new ProBlock(x).getLocation().equals(Metodos.stringToLocation(pro.getFiles().getBlocks().getString("block_" + id + ".loc")))){
                pro.getFiles().getBlocks().getStringList("block_" + id + ".admins").forEach(ad -> players.add(new ProPlayer(UUID.fromString(ad))));
                pro.getFiles().getBlocks().getStringList("block_" + id + ".users").forEach(ad -> players.add(new ProPlayer(UUID.fromString(ad))));
            }
        }
        return players;
    }

    public List<ProPlayer> getProtectionOwners(){
        List<ProPlayer> players = new ArrayList<>();

        for (int x = 0; x < pro.getFiles().getCurrentID("blocks"); x++){
            if (new ProBlock(x).getLocation().equals(Metodos.stringToLocation(pro.getFiles().getBlocks().getString("block_" + id + ".loc")))){
                pro.getFiles().getBlocks().getStringList("block_" + id + ".admins").forEach(ad -> players.add(new ProPlayer(UUID.fromString(ad))));
            }
        }
        return players;
    }

    public int getBlockID(){
        for (int x = 0; x < pro.getFiles().getCurrentID("blocks"); x++){
            if (new ProBlock(x).getLocation().equals(block.getLocation())){
                return x;
            }
        }
        return -1;
    }

    //Flags
    public HashMap<String, Boolean> getAllFlags(){
        HashMap<String, Boolean> settings = new HashMap<>();

        pro.getFiles().getAreas().getStringList("block_" + id + "flags").forEach(s -> settings.put(s, pro.getFiles().getAreas().getBoolean("block_" + id + "flags." + s)));

        return settings;
    }

    public void setFlag(String s, boolean value){
        String path = "block_" + id + "flags.";

        pro.getFiles().getAreas().set(path + s, value);
        pro.getFiles().saveFiles();
    }

    public boolean getFlag(String s){
        return pro.getFiles().getAreas().getBoolean("block_" + id + "flags." + s);
    }

    private void setDefaultFlags(){
        setFlag("useRedstone", false);
        setFlag("autoCloseDoors", false);
        setFlag("useHoppers", false);
    }

    //Utils
    public List<Material> getAllTypesToProtect(){
        List<Material> materials = new ArrayList<>();

        materials.add(Material.CHEST);
        materials.add(Material.TRAPPED_CHEST);
        materials.add(Material.FURNACE);
        materials.add(Material.BURNING_FURNACE);
        materials.add(Material.ANVIL);
        materials.add(Material.BREWING_STAND);
        materials.add(Material.TRAP_DOOR);
        materials.add(Material.IRON_TRAPDOOR);

        return materials;
    }
}
