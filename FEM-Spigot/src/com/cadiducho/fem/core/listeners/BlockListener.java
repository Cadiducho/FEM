package com.cadiducho.fem.core.listeners;

import com.cadiducho.fem.core.FEMCore;
import org.bukkit.event.Listener;

public class BlockListener implements Listener {
    
    public static FEMCore plugin;
    
    public BlockListener(FEMCore instance) {
        plugin = instance;
    }

}
