package com.github.dennermelo.core.tags.sql;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.setting.Settings;
import com.github.dennermelo.core.tags.sql.mysql.MySQLConnection;
import com.github.dennermelo.core.tags.sql.sqlite.SQLiteConnection;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLManager {

    private final CoreTags instance;
    private Connection connection;

    public SQLManager(CoreTags instance) {
        this.instance = instance;
    }

    public void openConnection() throws SQLException {
        FileConfiguration config = instance.getConfig();
        if (config.getString("Connection.type").equalsIgnoreCase("mysql")) {
            this.connection = new MySQLConnection(instance, Settings.MYSQL_HOST.asString(), Settings.MYSQL_PORT.asString(),
                    Settings.MYSQL_DATABASE.asString(), Settings.MYSQL_USER.asString(), Settings.MYSQL_PASS.asString()).openConnection();
        } else if (config.getString("Connection.type").equalsIgnoreCase("sqlite")) {
            this.connection = new SQLiteConnection(instance).getConnection();
        }
    }

    public boolean checkConnection() throws SQLException {
        return this.connection == null || this.connection.isClosed();
    }

    public Connection getConnection() throws SQLException {
        if (checkConnection()) openConnection();
        return this.connection;
    }

    public void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                instance.getLogger().log(Level.SEVERE, "Error closing the MySQL Connection!");
                e.printStackTrace();
            }
        }
    }

    public ResultSet querySQL(String query) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        closeConnection();
        return resultSet;
    }

    public void updateSQL(String update) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        try {
            statement.executeUpdate(update);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        closeConnection();
    }

}
