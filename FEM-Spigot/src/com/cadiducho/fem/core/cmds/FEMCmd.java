package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.FEMCore;
import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.Metodos;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public abstract class FEMCmd {
    
    private final transient String name;
    private final transient Grupo grupo;
    private final transient List<String> aliases;
    protected static transient FEMCore plugin = FEMCore.getInstance();
    protected static transient FEMServer server = new FEMServer();
    protected static transient Metodos metodos = FEMCore.getInstance().getMetodos();
    
    public FEMCmd(final String name, final Grupo grupo, final List<String> aliases) {
        this.name = name;
        this.grupo = grupo;
        this.aliases = aliases;
    }
    
    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }
    
    public Grupo getGroup() {
        return grupo;
    }
    
    public void run(ConsoleCommandSender sender, String label, String[] args) {
        run((CommandSender) sender, label, args);
    }
    
    public void run(FEMUser user, String label, String[] args) {
        run((CommandSender) user.getBase(), label, args);
    }
    
    public void run(CommandSender sender, String label, String[] args) {
        sender.sendMessage(Metodos.colorizar("&cEste comando no está funcional para este sender"));
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
    
    @Getter
    @AllArgsConstructor
    public enum Grupo {
        Usuario(0), //Gris
        Vip(1), //Amarillo
        VipSuper(2), //AmarilloOscuro
        VipMega(3), //Naranja
        YT(4),
        Helper(5), //Verde
        Admin(6), //Azul
        Owner(7), //Rojo
        Dev(8); //Turquesa

        private final int rank;
    }
}
