package com.cadiducho.fem.core;

import com.cadiducho.fem.core.cmds.*;
import com.cadiducho.fem.core.api.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

public class FEMCommands implements TabCompleter {

    public static List<FEMCmd> cmds = new ArrayList<>();
    public static FEMCommands ucmds;

    public static void load() {
        cmds.add(new CoreCMD());
        cmds.add(new AdminChatCMD());
        cmds.add(new AnvilCMD());
        cmds.add(new AfkCMD());
        cmds.add(new BackCMD());
        cmds.add(new BookCMD());
        cmds.add(new BroadcastCMD());
        cmds.add(new BurnCMD());
        cmds.add(new CnickCMD());
        cmds.add(new ClearCMD());
        cmds.add(new ClearArmorCMD());
        cmds.add(new DarRangoCMD());
        cmds.add(new EncantarCMD());
        cmds.add(new ExtinguishCMD());
        cmds.add(new FriendCMD());
        cmds.add(new FlyCMD());
        cmds.add(new GamemodeCMD());
        cmds.add(new GodCMD());
        cmds.add(new JumpCMD());
        cmds.add(new HatCMD());
        cmds.add(new HealCMD());
        cmds.add(new HelpOPCMD());
        cmds.add(new InvSeeCMD());
        cmds.add(new ItemDBCMD());
        cmds.add(new MeCMD());
        cmds.add(new MoreCMD());
        cmds.add(new NearCMD());
        cmds.add(new NickCMD());
        cmds.add(new ParkourCMD());
        cmds.add(new PingCMD());
        cmds.add(new RealNameCMD());
        cmds.add(new RepairCMD());
        cmds.add(new SeedCMD());
        cmds.add(new SignEditorCMD());
//        cmds.add(new StaffCMD());
        cmds.add(new SuicideCMD());
        cmds.add(new TeleportCMD());
        cmds.add(new TeleportAllCMD());
        cmds.add(new TeleportHereCMD());
        cmds.add(new TeleportAskCMD());
        cmds.add(new TeleportAskAllCMD());
        cmds.add(new TeleportAskHereCMD());
        cmds.add(new TeleportAcceptCMD());
        cmds.add(new TeleportDenyCMD());
        cmds.add(new LagCMD());
        cmds.add(new ListCMD());
        cmds.add(new LobbyCMD());
        cmds.add(new WarpCMD());
        cmds.add(new WeatherCMD());
        cmds.add(new SetWarpCMD());
        cmds.add(new SpawnerCMD());
        cmds.add(new SpeedCMD());
        cmds.add(new SetSpawnCMD());
        cmds.add(new SudoCMD());
        cmds.add(new DelWarpCMD());
        cmds.add(new WorkbenchCMD());
        cmds.add(new WhoisCMD());

        //
        ucmds = new FEMCommands();
        //
        cmds.forEach(cmd -> registrar(cmd));
    }

    public static void registrar(FEMCmd cmd) {
        CommandMap commandMap = getCommandMap();
        PluginCommand command = getCmd(cmd.getName());
        if (command.isRegistered()) {
            command.unregister(commandMap);
        }

        command.setAliases(cmd.getAliases());
        command.setTabCompleter(ucmds);

        if (commandMap == null) {
            return;
        }
        FEMCore.getInstance().debugLog("Registrando comando /" + cmd.getName());
        commandMap.register(FEMCore.getInstance().getDescription().getName(), command);
        
        //Añadir a la lista por si se registra desde otro plugin:
        if (!cmds.contains(cmd)) {
            cmds.add(cmd);
        }
        
        if (Bukkit.getPluginCommand("femcore:" + cmd.getName()) == null) {
            FEMCore.getInstance().log(Level.WARNING, "Error al cargar el comando /" + cmd.getName());
        }
    }

    private static PluginCommand getCmd(String name) {
        PluginCommand command = null;
        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);

            command = c.newInstance(name, FEMCore.getInstance());
        } catch (Exception e) {
        }
        return command;
    }

    public static void onCmd(final CommandSender sender, Command cmd, String label, final String[] args) {
        if (label.startsWith("femcore:")) {
            label = label.replaceFirst("femcore:", "");
        }
        for (FEMCmd cmdr : cmds) {
            if (label.equals(cmdr.getName()) || cmdr.getAliases().contains(label)) {
                if (sender instanceof ConsoleCommandSender) {
                    ConsoleCommandSender cs = (ConsoleCommandSender) sender;
                    cmdr.run(cs, label, args);
                    break;
                }
                if (sender instanceof Player) {
                    FEMUser p = FEMServer.getUser((Player) sender);
                    if (p.isOnRank(cmdr.getGroup())) {
                        cmdr.run(p, label, args);
                        return;
                    }

                    p.sendMessage("*noPermiso");
                    return;
                }
                cmdr.run(sender, label, args);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> rtrn = null;
        if (label.startsWith("femcore:")) {
            label = label.replaceFirst("femcore:", "");
        }
        /*
        * Auto Complete normal para cada comando si está declarado
         */
        for (FEMCmd cmdr : cmds) {
            if (cmdr.getName().equals(label) || cmdr.getAliases().contains(label)) {
                try {
                    if ((sender instanceof Player) && (!FEMServer.getUser((Player) sender).isOnRank(cmdr.getGroup()))) {
                        return new ArrayList<>();
                    }
                    rtrn = cmdr.onTabComplete(sender, cmd, label, args, args[args.length - 1], args.length - 1);
                } catch (Exception ex) {
                    FEMCore.getInstance().log("Fallo al autocompletar " + label);
                }
                break;
            }
        }
        /*
        * Si el autocomplete es null, que devuelva jugadores
         */
        if (rtrn == null) {
            rtrn = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                rtrn.add(p.getName());
            }
        }
        /*
        * Autocomplete para cada argumento
         */
        ArrayList<String> rtrn2 = new ArrayList<>();
        rtrn2.addAll(rtrn);
        rtrn = rtrn2;
        if (!(args[args.length - 1].isEmpty() || args[args.length - 1] == null)) {
            List<String> remv = new ArrayList<>();
            for (String s : rtrn) {
                if (!StringUtils.startsWithIgnoreCase(s, args[args.length - 1])) {
                    remv.add(s);
                }
            }
            rtrn.removeAll(remv);
        }
        return rtrn;
    }

    private static CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return commandMap;
    }
}
