package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.FEMFileLoader;
import java.util.Arrays;

public class CoreCMD extends FEMCmd {

    public CoreCMD() {
        super("femcore", Grupo.Usuario, Arrays.asList());
    }

    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (args.length == 1) {

            if (!user.isOnRank(Grupo.Moderador)) { //Si no tiene permisos de admin, cerrar con el default
                def(user);
                return;
            }

            //Subcomandos de administración
            switch (args[0].toLowerCase()) {
                case "toggledebug":
                    toggleDebug(user);
                    break;
                case "reloadconfig":
                    reloadConfig(user);
                    break;
            }
            return;
        }

        def(user);
    }

    private void def(FEMUser user) {
        user.sendMessage("&6FEM &afunciona con FEMCore " + "&7v" + plugin.getDescription().getVersion());
    }

    private void toggleDebug(FEMUser user) {
        plugin.getConfig().set("debug", !plugin.isDebug());
        plugin.saveConfig();

        String debug = (plugin.isDebug()) ? "&aActivado" : "&cDesactivado";
        user.sendMessage("&eHas cambiado el modo debug del FEMCore a: " + debug);
    }

    private void reloadConfig(FEMUser user) {
        plugin.reloadConfig();
        FEMFileLoader.reloadLang();
        user.sendMessage("&eConfiguración recargada");
    }
}
