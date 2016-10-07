package com.cadiducho.fem.lobby;

import com.cadiducho.fem.core.api.FEMUser;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Clase para controlar los teams en el lobby
 * @author Cadiducho
 */
public class LobbyTeams {
    
    public static Scoreboard board;
    public static Team tUsuario;
    public static Team tVip;
    public static Team tVipSuper;
    public static Team tVipMega;
    public static Team tHelper;
    public static Team tAdmin;
    public static Team tOwner;
    public static Team tDev;
    
    public static void initTeams() {
        board = Bukkit.getScoreboardManager().getMainScoreboard();

        tOwner = board.getTeam("1Core_Own") == null ? board.registerNewTeam("1Core_Own") : board.getTeam("1Core_Own");
        tAdmin = board.getTeam("2Core_Admin") == null ? board.registerNewTeam("2Core_Admin") : board.getTeam("2Core_Admin");
        tDev = board.getTeam("3Core_Dev") == null ? board.registerNewTeam("3Core_Tecnicos") : board.getTeam("3Core_Dev");
        tHelper = board.getTeam("4Core_Help") == null ? board.registerNewTeam("4Core_Help") : board.getTeam("4Core_Help");
        tVipMega = board.getTeam("5Core_VMega") == null ? board.registerNewTeam("5Core_VMega") : board.getTeam("5Core_VMega");
        tVipSuper = board.getTeam("6Core_VSuper") == null ? board.registerNewTeam("6Core_VSuper") : board.getTeam("6Core_VSuper");
        tVip = board.getTeam("7Core_Vip") == null ? board.registerNewTeam("7Core_Vip") : board.getTeam("7Core_Vip");
        tUsuario = board.getTeam("8Core_User") == null ? board.registerNewTeam("8Core_User") : board.getTeam("8Core_User");

        tOwner.setPrefix("§4[OWN] ");
        tAdmin.setPrefix("§c[ADM] ");
        tDev.setPrefix("§b[DEV] ");
        tHelper.setPrefix("§2[HELP] ");
        tVipMega.setPrefix("§6[MEGA] ");
        tVipSuper.setPrefix("§a[SUPER] ");
        tVip.setPrefix("§e[VIP] ");
        tUsuario.setPrefix("§f");
        
        tOwner.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        tAdmin.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        tDev.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        tHelper.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        tVipMega.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        tVipSuper.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        tVip.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        tUsuario.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
    }
    
    public static void setScoreboardTeam(FEMUser user) {
        removeScoreboardTeam(user);
        switch(user.getUserData().getGrupo()) {
            case Owner:
                tOwner.addEntry(user.getName()); break;
            case Admin:
                tAdmin.addEntry(user.getName()); break;
            case Dev:
                tDev.addEntry(user.getName()); break;
            case Helper:
                tHelper.addEntry(user.getName()); break;
            case VipMega:
                tVipMega.addEntry(user.getName()); break;
            case VipSuper:
                tVipSuper.addEntry(user.getName()); break;
            case Vip:
                tVip.addEntry(user.getName()); break;
            default:
                tUsuario.addEntry(user.getName()); break;
        }
    }
    
    public static     void removeScoreboardTeam(FEMUser user) {
        Team t = board.getEntryTeam(user.getName());
        if (t != null) t.removeEntry(user.getName());
    }
    
}
