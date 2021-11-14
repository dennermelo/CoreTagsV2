package com.github.dennermelo.core.tags.sql.sqlite;

import com.github.dennermelo.core.tags.CoreTags;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLiteConnection {

    private Connection connection;
    private final CoreTags instance;

    public SQLiteConnection(CoreTags instance) {
        this.instance = instance;
    }

    public void openConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            File file = new File(instance.getDataFolder(), "database.db");
            if (!file.exists()) {
                file.createNewFile();
            }
            String url = "jdbc:sqlite:" + file;
            connection = java.sql.DriverManager.getConnection(url);
            instance.getLogger().info("SQLite conectado com sucesso!");
        } catch (SQLException e) {
            instance.getLogger().log(Level.SEVERE, "Nao foi possivel conectar-se ao servidor SQLite, motivo: " + e.getMessage());
        } catch (IOException e) {
            instance.getLogger().log(Level.SEVERE, "Nao foi possivel criar o arquivo de banco de dados, motivo: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            instance.getLogger().log(Level.SEVERE, "Nao foi possivel carregar o driver do SQLite, motivo: " + e.getMessage());
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
