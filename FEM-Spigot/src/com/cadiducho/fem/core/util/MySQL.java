package com.cadiducho.fem.core.util;

import com.cadiducho.fem.core.FEMCore;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.api.FEMUser.UserData;
import com.cadiducho.fem.core.cmds.FEMCmd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

/**
 * Objeto para conexiones de MySQL
 *
 * @author Huskehhh base original, Cadiducho actualización y metodos
 */
public class MySQL {

    protected Connection connection;

    private final String user, database, password, port, hostname;

    public MySQL(String hostname, String port, String database, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.user = username;
        this.password = password;
    }

    public boolean checkConnection() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean closeConnection() throws SQLException {
        if (connection == null) {
            return false;
        }
        connection.close();
        return true;
    }

    public Connection openConnection() throws SQLException, ClassNotFoundException {
        if (checkConnection()) {
            return connection;
        }

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"
                + this.hostname + ":" + this.port + "/" + this.database, this.user, this.password);
        return connection;
    }

    // -----------------
    public void setupTable(Player p) {
        FEMCore.getInstance().getServer().getScheduler().runTaskAsynchronously(FEMCore.getInstance(), () -> {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT `id` FROM `fem_datos` WHERE `uuid` = ?");
                statement.setString(1, p.getUniqueId().toString());
                ResultSet rs = statement.executeQuery();
                if (!rs.next()) { //No hay filas encontradas, insertar nuevos datos
                    try {
                        PreparedStatement inserDatos = connection.prepareStatement(
                                "INSERT INTO `fem_datos` (`uuid`, `name`, `grupo`) VALUES (?, ?, ?)");
                        inserDatos.setString(1, p.getUniqueId().toString());
                        inserDatos.setString(2, p.getName());
                        inserDatos.setInt(3, 0);
                        inserDatos.executeUpdate();

                        PreparedStatement inserStats = connection.prepareStatement(
                                "INSERT INTO `fem_stats` (`uuid`) VALUES (?)");
                        inserStats.setString(1, p.getUniqueId().toString());
                        inserStats.executeUpdate();

                        PreparedStatement inserSettings = connection.prepareStatement(
                                "INSERT INTO `fem_settings` (`uuid`) VALUES (?)");
                        inserSettings.setString(1, p.getUniqueId().toString());
                        inserSettings.executeUpdate();

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void saveUser(FEMUser u) {
        FEMCore.getInstance().getServer().getScheduler().runTaskAsynchronously(FEMCore.getInstance(), () -> {
            UserData data = u.getUserData();
            try {

                PreparedStatement statementDatos = connection.prepareStatement("UPDATE `fem_datos` SET `grupo`=?,`god`=?,`coins`=?,`lastConnect`=?,`ip`=?,`nick`=? WHERE `uuid`=?");
                statementDatos.setInt(1, data.getGrupo() != null ? data.getGrupo().getRank() : 0);
                statementDatos.setBoolean(2, data.getGod() == null ? false : data.getGod());
                statementDatos.setInt(3, data.getCoins() == null ? 0 : data.getCoins());
                statementDatos.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime())); 
                statementDatos.setString(5, data.getIp() == null ? "" : data.getIp().getAddress().getHostAddress());
                statementDatos.setString(6, data.getNickname() == null ? "" : data.getNickname());
                statementDatos.setString(7, u.getUuid().toString());
                statementDatos.executeUpdate();

                //Stats
                PreparedStatement statementStats = connection.prepareStatement("UPDATE `fem_stats` SET `kills_tnt`=?,`kills_gh`=?,`deaths_tnt`=?,`deaths_gh`=?,`jugadas_tnt`=?,"
                        + "`jugadas_dod`=?,`jugadas_gh`=?,`ganadas_tnt`=?,`ganadas_dod`=?,`ganadas_gh`=?,`tntPuestas`=?,`tntQuitadas`=?,`tntExplotadas`=?,`genUpgraded`=?,"
                        + " `gemDestroyed`=?,`gemPlanted`=?,`record_dod`=?,`rondas_dod`=? WHERE `uuid`=?");
                statementStats.setInt(1, data.getKills().get(1));
                statementStats.setInt(2, data.getKills().get(3));
                statementStats.setInt(3, data.getDeaths().get(1));
                statementStats.setInt(4, data.getDeaths().get(3));
                statementStats.setInt(5, data.getPlays().get(1));
                statementStats.setInt(6, data.getPlays().get(2));
                statementStats.setInt(7, data.getPlays().get(3));
                statementStats.setInt(8, data.getWins().get(1));
                statementStats.setInt(9, data.getWins().get(2));
                statementStats.setInt(10, data.getWins().get(3));
                statementStats.setInt(11, data.getTntPuestas());
                statementStats.setInt(12, data.getTntQuitadas());
                statementStats.setInt(13, data.getTntExplotadas());
                statementStats.setInt(14, data.getGenUpgraded());
                statementStats.setInt(15, data.getGemDestroyed());
                statementStats.setInt(16, data.getGemPlanted());
                statementStats.setInt(17, data.getRecord_dod());
                statementStats.setInt(18, data.getRondas_dod());
                statementStats.setString(19, u.getUuid().toString());
                statementStats.executeUpdate();

                //Settings
                PreparedStatement statementSett = connection.prepareStatement("UPDATE `fem_settings` SET `friendRequest`=?,`hideMode`=? WHERE `uuid`=?");
                statementSett.setBoolean(1, data.getFriendRequest() == null ? true : data.getFriendRequest());
                statementSett.setInt(2, data.getHideMode() == null ? 1 : data.getHideMode());
                statementSett.setString(3, u.getUuid().toString());
                statementSett.executeUpdate();

            } catch (Exception ex) {
                System.out.println("Ha ocurrido un error guardando los datos de " + u.getBase().getName());
                ex.printStackTrace();
            }
        });
    }

    public UserData loadUserData(UUID id) {
        UserData data = new UserData();
        try {

            //Datos
            PreparedStatement statementDatos = connection.prepareStatement("SELECT `timeJoin`,`grupo`,`god`,`coins`,`lastConnect` FROM `fem_datos` WHERE `uuid` = ?");
            statementDatos.setString(1, id.toString());
            ResultSet rsDatos = statementDatos.executeQuery();

            if (rsDatos.next()) {
                int rank = rsDatos.getInt("grupo");
                data.setGrupo(FEMCmd.Grupo.values()[rank] == null ? FEMCmd.Grupo.Usuario : FEMCmd.Grupo.values()[rank]);
                data.setTimeJoin(rsDatos.getLong("timeJoin"));
                data.setGod(rsDatos.getBoolean("god"));
                data.setCoins(rsDatos.getInt("coins"));
                data.setLastConnect(rsDatos.getLong("lastConnect"));
            }
            
            //Stats
            PreparedStatement statementStats = connection.prepareStatement("SELECT * FROM `fem_stats` WHERE `uuid` = ?");
            statementStats.setString(1, id.toString());
            ResultSet rsStats = statementStats.executeQuery();    
            if (rsStats.next()) {  
                
                HashMap<Integer, Integer> kills = new HashMap<>();
                HashMap<Integer, Integer> deaths = new HashMap<>();
                HashMap<Integer, Integer> wins = new HashMap<>();
                HashMap<Integer, Integer> plays = new HashMap<>();
                
                kills.put(1, rsStats.getInt("kills_tnt"));
                kills.put(3, rsStats.getInt("kills_gh"));
                
                deaths.put(1, rsStats.getInt("deaths_tnt"));
                deaths.put(3, rsStats.getInt("deaths_gh"));
                
                wins.put(1, rsStats.getInt("ganadas_tnt"));
                wins.put(2, rsStats.getInt("ganadas_dod"));
                wins.put(3, rsStats.getInt("ganadas_gh"));
                
                plays.put(1, rsStats.getInt("jugadas_tnt"));
                plays.put(2, rsStats.getInt("jugadas_dod"));
                plays.put(3, rsStats.getInt("jugadas_gh"));
                
                data.setKills(kills);
                data.setDeaths(deaths);
                data.setWins(wins);
                data.setPlays(plays);
                
                data.setTntPuestas(rsStats.getInt("tntPuestas"));
                data.setTntQuitadas(rsStats.getInt("tntQuitadas"));
                data.setTntExplotadas(rsStats.getInt("tntExplotadas"));
                data.setGenUpgraded(rsStats.getInt("genUpgraded"));
                data.setGemDestroyed(rsStats.getInt("gemDestroyed"));
                data.setGemPlanted(rsStats.getInt("gemPlanted"));
                data.setRecord_dod(rsStats.getInt("record_dod"));
                data.setRondas_dod(rsStats.getInt("rondas_dod"));
                
            }
            
            //Settings
            PreparedStatement statementSett = connection.prepareStatement("SELECT * FROM `fem_settings` WHERE `uuid` = ?");
            statementSett.setString(1, id.toString());
            ResultSet rsSett = statementSett.executeQuery();
            
            if (rsSett.next()) {
                data.setFriendRequest(rsSett.getBoolean("friendRequest"));
                data.setHideMode(rsSett.getInt("hideMode"));
            }
            
            //Amigos
            PreparedStatement statementAmigos = connection.prepareStatement("SELECT `to` FROM `fem_amigos` WHERE `uuid` = ?");
            statementAmigos.setString(1, id.toString());
            ResultSet rsAmigos = statementAmigos.executeQuery();
            
            ArrayList<UUID> amigos = new ArrayList<>();
            while (rsAmigos.next()) {
                amigos.add(UUID.fromString(rsAmigos.getString("to")));        
            }
            data.setAmigos(amigos);

        } catch (Exception ex) {
            System.out.println("Ha ocurrido un error cargando los datos de " + id);
            ex.printStackTrace();
        }   
        return data;
    }
    
    public void addFriend(FEMUser uuid, FEMUser to) {
        FEMCore.getInstance().getServer().getScheduler().runTaskAsynchronously(FEMCore.getInstance(), () -> {
            try {
                PreparedStatement statementAmigos = connection.prepareStatement("INSERT INTO `fem_amigos` (`uuid`, `to`) VALUES (?, ?)");
                statementAmigos.setString(1, uuid.getUuid().toString());
                statementAmigos.setString(2, to.getUuid().toString());
                statementAmigos.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public void removeFriend(FEMUser uuid, FEMUser to) {
        FEMCore.getInstance().getServer().getScheduler().runTaskAsynchronously(FEMCore.getInstance(), () -> {
            try {
                PreparedStatement statementAmigos = connection.prepareStatement("DELETE FROM `fem_amigos` WHERE `uuid`=? AND `to`=?");
                statementAmigos.setString(1, uuid.getUuid().toString());
                statementAmigos.setString(2, to.getUuid().toString());
                statementAmigos.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
