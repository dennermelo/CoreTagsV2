package com.github.dennermelo.core.tags.sql.mysql;


import com.github.dennermelo.core.tags.CoreTags;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.logging.Level;

public class MySQLConnection {

    private final CoreTags instance;

    private final String user;
    private final String database;
    private final String password;
    private final String port;
    private final String hostname;
    private Connection connection;

    public MySQLConnection(CoreTags instance, String hostname, String port, String database, String username, String password) {
        this.instance = instance;
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.user = username;
        this.password = password;
        this.connection = null;
    }

    public Connection openConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.user, this.password);
            instance.getLogger().info("MySQL connection established!");
        } catch (SQLException e) {
            instance.getLogger().log(Level.SEVERE, "Nao foi possivel conectar-se ao servidor MySQL, motivo: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            instance.getLogger().log(Level.SEVERE, "Driver JDBC nao encontrado!");
        }
        return this.connection;
    }
}