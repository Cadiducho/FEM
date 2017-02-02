package com.cadiducho.fem.core.util;

import com.cadiducho.fem.core.FEMCore;
import com.cadiducho.fem.core.api.FEMServer.GameID;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.api.FEMUser.UserData;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                + this.hostname + ":" + this.port + "/" + this.database + "?autoReconnect=true", this.user, this.password);
        return connection;
    }

    // -----------------
    public void setupTable(Player p) {
        FEMCore.getInstance().getServer().getScheduler().runTaskAsynchronously(FEMCore.getInstance(), () -> {
            try {
                PreparedStatement statement = openConnection().prepareStatement("SELECT `id` FROM `fem_datos` WHERE `uuid` = ?");
                statement.setString(1, p.getUniqueId().toString());
                ResultSet rs = statement.executeQuery();
                if (!rs.next()) { //No hay filas encontradas, insertar nuevos datos
    
                    Integer lang = 0;
                    
                    //No comprobar pais de momento
                    /*URL url = new URL("http://api.predator.wtf/geoip/?arguments=" + p.getAddress().getHostName());
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                    String strTemp = "";
                    

                    while ((strTemp = br.readLine()) != null) {
                        if (strTemp.contains("Country: ")) {
                            String country = strTemp.replace("Country: ", "");
                            switch (country.toLowerCase()) {
                                case "france": lang = 1; break;
                                case "italy": lang = 2; break;
                            }
                        }
                    } */

                    PreparedStatement inserDatos = openConnection().prepareStatement(
                            "INSERT INTO `fem_datos` (`uuid`, `name`, `grupo`) VALUES (?, ?, ?)");
                    inserDatos.setString(1, p.getUniqueId().toString());
                    inserDatos.setString(2, p.getName());
                    inserDatos.setInt(3, 0);
                    inserDatos.executeUpdate();

                    PreparedStatement inserStats = openConnection().prepareStatement(
                            "INSERT INTO `fem_stats` (`uuid`) VALUES (?)");
                    inserStats.setString(1, p.getUniqueId().toString());
                    inserStats.executeUpdate();

                    PreparedStatement inserSettings = openConnection().prepareStatement(
                            "INSERT INTO `fem_settings` (`uuid`, `lang`) VALUES (?, ?)");
                    inserSettings.setString(1, p.getUniqueId().toString());
                    inserSettings.setInt(2, lang);
                    inserSettings.executeUpdate();

                }
            } catch (SQLException | ClassNotFoundException /*| IOException*/ ex) {
                ex.printStackTrace();
            }
        });
    }

    public void saveUser(FEMUser u) {
        FEMCore.getInstance().getServer().getScheduler().runTaskAsynchronously(FEMCore.getInstance(), () -> {
            UserData data = u.getUserData();
            try {

                PreparedStatement statementDatos = openConnection().prepareStatement("UPDATE `fem_datos` SET `grupo`=?,`god`=?,`coins`=?,`lastConnect`=?,`ip`=?,`nick`=? WHERE `uuid`=?");
                statementDatos.setInt(1, data.getGrupo() != null ? data.getGrupo().getRank() : 0);
                statementDatos.setBoolean(2, data.getGod() == null ? false : data.getGod());
                statementDatos.setInt(3, data.getCoins() == null ? 0 : data.getCoins());
                statementDatos.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
                statementDatos.setString(5, data.getIp() == null ? "" : data.getIp().getAddress().getHostAddress());
                statementDatos.setString(6, data.getNickname() == null ? "" : data.getNickname());
                statementDatos.setString(7, u.getUuid().toString());
                statementDatos.executeUpdate();

                //Stats
                PreparedStatement statementStats = openConnection().prepareStatement("UPDATE `fem_stats` SET `kills_tnt`=?,`kills_gh`=?,`deaths_tnt`=?,`deaths_gh`=?,`jugadas_tnt`=?,"
                        + "`jugadas_dod`=?,`jugadas_gh`=?,`ganadas_tnt`=?,`ganadas_dod`=?,`ganadas_gh`=?,`tntPuestas`=?,`tntQuitadas`=?,`tntExplotadas`=?,`genUpgraded`=?,"
                        + "`gemDestroyed`=?,`gemPlanted`=?,`record_dod`=?,`rondas_dod`=?,`picAcertadas`=?,`picDibujadas`=?,`picPuntosTotales`=?,`ganadas_pic`=?,`jugadas_pic`=?,"
                        + "`jugadas_br`=?,`ganadas_br`=?,`kills_br`=?,`deaths_br`=?,`brIntercambios`=?,`jugadas_lg`=?,`ganadas_lg`=?,`kills_lg`=?,`deaths_lg`=?,`luckyRotos`=?,"
                        + "`ganadas_dro`=?, `deaths_dro`=?, `jugadas_dro`=?, `timePlayed`=? "
                        + "WHERE `uuid`=?");

                //TODO Reorganizar query y usar nuevos metodos de stats
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
                statementStats.setInt(19, data.getPicAcertadas());
                statementStats.setInt(20, data.getPicDibujadas());
                statementStats.setInt(21, data.getPicPuntosTotales());
                statementStats.setInt(22, data.getWins().get(4));
                statementStats.setInt(23, data.getPlays().get(4));
                statementStats.setInt(24, data.getPlays().get(5));
                statementStats.setInt(25, data.getWins().get(5));
                statementStats.setInt(26, data.getKills().get(5));
                statementStats.setInt(27, data.getDeaths().get(5));
                statementStats.setInt(28, data.getBrIntercambios());
                statementStats.setInt(29, data.getPlays().get(6));
                statementStats.setInt(30, data.getWins().get(6));
                statementStats.setInt(31, data.getKills().get(6));
                statementStats.setInt(32, data.getDeaths().get(6));
                statementStats.setInt(33, data.getLuckyRotos());
                statementStats.setInt(34, data.getWins(GameID.DROPPER));
                statementStats.setInt(35, data.getDeaths(GameID.DROPPER));
                statementStats.setInt(36, data.getPlays(GameID.DROPPER));
                statementStats.setLong(37, data.getTimePlayed());
                statementStats.setString(38, u.getUuid().toString());
                statementStats.executeUpdate();

                for (Map.Entry<String, Integer> entry : data.getDropper().entrySet()) {
                    PreparedStatement statement = openConnection().prepareStatement("SELECT `times` FROM `fem_dropper` WHERE `uuid` =? AND `mapa`=?");
                    statement.setString(1, u.getUuid().toString());
                    statement.setString(2, entry.getKey());
                    ResultSet rs = statement.executeQuery();
                    if (!rs.next()) { //No hay filas encontradas, insertar nuevos datos
                        PreparedStatement statementDropper = openConnection().prepareStatement("INSERT INTO `fem_dropper` (`mapa`,`times`,`uuid`) VALUES (?,?,?)");
                        statementDropper.setString(1, entry.getKey());
                        statementDropper.setInt(2, entry.getValue());
                        statementDropper.setString(3, u.getUuid().toString());
                        statementDropper.executeUpdate();
                    } else {
                        PreparedStatement statementDropper = openConnection().prepareStatement("UPDATE `fem_dropper` SET `times`=? WHERE `uuid`=? AND `mapa`=?");
                        statementDropper.setInt(1, entry.getValue());
                        statementDropper.setString(2, u.getUuid().toString());
                        statementDropper.setString(3, entry.getKey());
                        statementDropper.executeUpdate();
                    }
                }

                //Settings
                PreparedStatement statementSett = openConnection().prepareStatement("UPDATE `fem_settings` SET `friendRequest`=?,`hideMode`=?,`lang`=?,`enableTell`=? WHERE `uuid`=?");
                statementSett.setBoolean(1, data.getFriendRequest() == null ? true : data.getFriendRequest());
                statementSett.setInt(2, data.getHideMode() == null ? 2 : data.getHideMode());
                statementSett.setInt(3, data.getLang() == null ? 0 : data.getLang());
                statementSett.setBoolean(4, data.getEnableTell() == null ? true : data.getEnableTell());
                statementSett.setString(5, u.getUuid().toString());
                statementSett.executeUpdate();

            } catch (Exception ex) {
                System.out.println("Ha ocurrido un error guardando los datos de " + u.getName());
                ex.printStackTrace();
            }
        });
    }

    public UserData loadUserData(UUID id) {
        UserData data = new UserData();
        try {

            //Datos
            PreparedStatement statementDatos = openConnection().prepareStatement("SELECT `timeJoin`,`grupo`,`god`,`coins`,`lastConnect` FROM `fem_datos` WHERE `uuid` = ?");
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
            PreparedStatement statementStats = openConnection().prepareStatement("SELECT * FROM `fem_stats` WHERE `uuid` = ?");
            statementStats.setString(1, id.toString());
            ResultSet rsStats = statementStats.executeQuery();    
            if (rsStats.next()) {  
                
                HashMap<Integer, Integer> kills = new HashMap<>();
                HashMap<Integer, Integer> deaths = new HashMap<>();
                HashMap<Integer, Integer> wins = new HashMap<>();
                HashMap<Integer, Integer> plays = new HashMap<>();
                
                kills.put(1, rsStats.getInt("kills_tnt"));
                kills.put(3, rsStats.getInt("kills_gh"));
                kills.put(5, rsStats.getInt("kills_br"));
                kills.put(6, rsStats.getInt("kills_lg"));
                
                deaths.put(1, rsStats.getInt("deaths_tnt"));
                deaths.put(3, rsStats.getInt("deaths_gh"));
                deaths.put(5, rsStats.getInt("deaths_br"));
                deaths.put(6, rsStats.getInt("deaths_lg"));
                deaths.put(GameID.DROPPER.getId(), rsStats.getInt("deaths_dro"));
                
                wins.put(1, rsStats.getInt("ganadas_tnt"));
                wins.put(2, rsStats.getInt("ganadas_dod"));
                wins.put(3, rsStats.getInt("ganadas_gh"));
                wins.put(4, rsStats.getInt("ganadas_pic"));
                wins.put(5, rsStats.getInt("ganadas_br"));
                wins.put(6, rsStats.getInt("ganadas_lg"));
                wins.put(GameID.DROPPER.getId(), rsStats.getInt("ganadas_dro"));
                
                plays.put(1, rsStats.getInt("jugadas_tnt"));
                plays.put(2, rsStats.getInt("jugadas_dod"));
                plays.put(3, rsStats.getInt("jugadas_gh"));
                plays.put(4, rsStats.getInt("jugadas_pic"));
                plays.put(5, rsStats.getInt("jugadas_br"));
                plays.put(6, rsStats.getInt("jugadas_lg"));
                plays.put(GameID.DROPPER.getId(), rsStats.getInt("jugadas_dro"));
                
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
                data.setPicAcertadas(rsStats.getInt("picAcertadas"));
                data.setPicDibujadas(rsStats.getInt("picDibujadas"));
                data.setPicPuntosTotales(rsStats.getInt("picPuntosTotales"));
                data.setBrIntercambios(rsStats.getInt("brIntercambios"));
                data.setLuckyRotos(rsStats.getInt("luckyRotos"));
                
                data.setTimePlayed(rsStats.getLong("timePlayed"));
            }
            
            //Settings
            PreparedStatement statementSett = openConnection().prepareStatement("SELECT * FROM `fem_settings` WHERE `uuid` = ?");
            statementSett.setString(1, id.toString());
            ResultSet rsSett = statementSett.executeQuery();
            
            if (rsSett.next()) {
                data.setFriendRequest(rsSett.getBoolean("friendRequest"));
                data.setHideMode(rsSett.getInt("hideMode"));
                data.setLang(rsSett.getInt("lang"));
                data.setEnableTell(rsSett.getBoolean("enableTell"));
            }

            //Dropper
            PreparedStatement statementDropper = openConnection().prepareStatement("SELECT `mapa`,`times` FROM `fem_dropper` WHERE `uuid` = ?");
            statementDropper.setString(1, id.toString());
            ResultSet rsDropper = statementDropper.executeQuery();

            HashMap<String, Integer> dropper = new HashMap<>();
            while (rsDropper.next()) {
                dropper.put(rsDropper.getString("mapa"), rsDropper.getInt("times"));
            }
            data.setDropper(dropper);
            PreparedStatement statementDropperInsignias = openConnection().prepareStatement("SELECT `mapa` FROM `fem_dropperInsignias` WHERE `uuid` = ?");
            statementDropperInsignias.setString(1, id.toString());
            ResultSet rsDropInsignias = statementDropperInsignias.executeQuery();
            ArrayList<String> insignias = new ArrayList<>();
            while (rsDropInsignias.next()) {
                insignias.add(rsDropInsignias.getString("mapa"));        
            }
            data.setDropperInsignias(insignias);
            
            //Amigos
            PreparedStatement statementAmigos = openConnection().prepareStatement("SELECT `to` FROM `fem_amigos` WHERE `uuid` = ?");
            statementAmigos.setString(1, id.toString());
            ResultSet rsAmigos = statementAmigos.executeQuery();
            
            ArrayList<UUID> amigos = new ArrayList<>();
            while (rsAmigos.next()) {
                amigos.add(UUID.fromString(rsAmigos.getString("to")));        
            }
            data.setAmigos(amigos);
        } catch (CommunicationsException ex) {
            //Si el driver ha perdido la conexion (timeout) cerrar, abrir y volver a intentar
            FEMCore.getInstance().debugLog(ex.toString());
            try {
                closeConnection();
                openConnection();
                return loadUserData(id);
            } catch (Exception ex1) {
                ex1.printStackTrace();
            }
        } catch (Exception ex) {
            System.out.println("Ha ocurrido un error cargando los datos de " + id);
            ex.printStackTrace();
        }
        return data;
    }
    
    public void addFriend(FEMUser uuid, FEMUser to) {
        FEMCore.getInstance().getServer().getScheduler().runTaskAsynchronously(FEMCore.getInstance(), () -> {
            try {
                PreparedStatement statementAmigos = openConnection().prepareStatement("INSERT INTO `fem_amigos` (`uuid`, `to`) VALUES (?, ?)");
                statementAmigos.setString(1, uuid.getUuid().toString());
                statementAmigos.setString(2, to.getUuid().toString());
                statementAmigos.executeUpdate();
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public void removeFriend(FEMUser uuid, FEMUser to) {
        FEMCore.getInstance().getServer().getScheduler().runTaskAsynchronously(FEMCore.getInstance(), () -> {
            try {
                PreparedStatement statementAmigos = openConnection().prepareStatement("DELETE FROM `fem_amigos` WHERE `uuid`=? AND `to`=?");
                statementAmigos.setString(1, uuid.getUuid().toString());
                statementAmigos.setString(2, to.getUuid().toString());
                statementAmigos.executeUpdate();
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public boolean checkInsignia(FEMUser u, String mapa, boolean insert) {
        try {
            PreparedStatement statement = openConnection().prepareStatement("SELECT * FROM `fem_dropperInsignias` WHERE `uuid` =? AND `mapa`=?");
            statement.setString(1, u.getUuid().toString());
            statement.setString(2, mapa);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) { //No hay filas encontradas, insertar nuevos datos
                if (insert) {
                    PreparedStatement statementAmigos = openConnection().prepareStatement("INSERT INTO `fem_dropperInsignias` (`uuid`, `mapa`) VALUES (?, ?)");
                    statementAmigos.setString(1, u.getUuid().toString());
                    statementAmigos.setString(2, mapa);
                    statementAmigos.executeUpdate();
                }
                return false;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public HashMap<FEMUser, Integer> get10Top(String search){
        HashMap<FEMUser, Integer> winners = new HashMap<>();
        HashMap<FEMUser, Integer> top = new HashMap<>();

        try {
            Statement s = openConnection().createStatement();
            s.executeQuery("SELECT uuid, " + search + " FROM fem_stats");
            ResultSet rs = s.getResultSet ();
            while (rs.next ()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                int kills = rs.getInt("kills_lg");
                winners.put(new FEMUser(uuid), kills);
            }
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

        List<Integer> list = new ArrayList<>(winners.values());
        Collections.sort(list, Collections.reverseOrder());

        winners.keySet().forEach(k -> list.subList(0, 10).forEach(v -> {
                if (winners.get(k).equals(v)){
                    top.put(k, v);
                }
            }));

        return top;
    }
}
