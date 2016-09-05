package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;

public class WorkbenchCMD extends FEMCmd {
    
    public WorkbenchCMD() {
        super("workbench", Grupo.Vip, Arrays.asList("wbench"));
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        user.getPlayer().openWorkbench(null, true);
    }
    
}
