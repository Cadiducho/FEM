package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;

public class LobbyCMD extends FEMCmd {

    public LobbyCMD() {
        super("lobby", Grupo.Usuario, Arrays.asList("salir", "quit", "exit", "volver"));
    }

    @Override
    public void run(FEMUser user, String label, String[] args) {
        user.sendMessage("&6Volviendo al Lobby...");
        user.sendToLobby();
    }
}
