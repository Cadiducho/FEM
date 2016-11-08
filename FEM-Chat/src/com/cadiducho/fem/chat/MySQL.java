package com.cadiducho.fem.chat;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
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
import net.md_5.bungee.api.ProxyServer;

/**
 * Objeto para conexiones de MySQL
 *
 * @author Cadiducho
 */
public class MySQL {

    protected Connection connection;

    private final String user, database, password, port, hostname;

    public MySQL(String hostname, String port, String database, String username, String password) {
        this.hostname = hostname;
        this.port = "3306";
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
    public HashMap<UUID, ArrayList<UUID>> loadIgnoredList() {
        HashMap<UUID, ArrayList<UUID>> ignorados = new HashMap<>();
        
        try {
            PreparedStatement statementUuid = openConnection().prepareStatement("SELECT `uuid` FROM `fem_ignorados`");
            ResultSet rsUuid = statementUuid.executeQuery();
              
            while (rsUuid.next()) {
                PreparedStatement statementAmigos = openConnection().prepareStatement("SELECT `to` FROM `fem_ignorados` WHERE `uuid` = ?");
                statementAmigos.setString(1, rsUuid.getString("uuid"));
                ResultSet rsIgnorados = statementAmigos.executeQuery();
                ArrayList<UUID> igno = new ArrayList<>();
                while (rsIgnorados.next()) {
                    igno.add(UUID.fromString(rsIgnorados.getString("to")));
                }
                ignorados.put(UUID.fromString(rsUuid.getString("uuid")), igno);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Ha ocurrido un error cargando la lista de ignorados");
            System.out.println(ex.getMessage());
        }
        return ignorados;
    }
    
    public void addIgnore(UUID uuid, UUID to) {
        ProxyServer.getInstance().getScheduler().runAsync(FEMChat.getInstance(), () -> {
            try {
                PreparedStatement statementAmigos = openConnection().prepareStatement("INSERT INTO `fem_ignorados` (`uuid`, `to`) VALUES (?, ?)");
                statementAmigos.setString(1, uuid.toString());
                statementAmigos.setString(2, to.toString());
                statementAmigos.executeUpdate();
            } catch (CommunicationsException ex) {
                //Si el driver ha perdido la conexion (timeout) cerrar, abrir y volver a intentar
                try {
                    closeConnection();
                    openConnection();
                    addIgnore(uuid, to);
                } catch (Exception ex1) {
                    ex1.printStackTrace();
                }
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public void removeIgnore(UUID uuid, UUID to) {
        ProxyServer.getInstance().getScheduler().runAsync(FEMChat.getInstance(), () -> {
            try {
                PreparedStatement statementAmigos = openConnection().prepareStatement("DELETE FROM `fem_ignorados` WHERE `uuid`=? AND `to`=?");
                statementAmigos.setString(1, uuid.toString());
                statementAmigos.setString(2, to.toString());
                statementAmigos.executeUpdate();
            } catch (CommunicationsException ex) {
                //Si el driver ha perdido la conexion (timeout) cerrar, abrir y volver a intentar
                try {
                    closeConnection();
                    openConnection();
                    removeIgnore(uuid, to);
                } catch (Exception ex1) {
                    ex1.printStackTrace();
                } 
            }catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public void updateDisableTellList() { 
        ProxyServer.getInstance().getScheduler().runAsync(FEMChat.getInstance(), () -> {
            try {
                ArrayList<UUID> lista = new ArrayList<>();
                PreparedStatement statement = openConnection().prepareStatement("SELECT `uuid` FROM `fem_settings` WHERE `enableTell` = '1'");
                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    lista.add(UUID.fromString(rs.getString("uuid")));
                }
                FEMChat.getInstance().setDisableTell(lista);
            } catch (CommunicationsException ex) {
                //Si el driver ha perdido la conexion (timeout) cerrar, abrir y volver a intentar
                try {
                    closeConnection();
                    openConnection();
                    updateDisableTellList();
                } catch (Exception ex1) {
                    ex1.printStackTrace();
                }            
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
