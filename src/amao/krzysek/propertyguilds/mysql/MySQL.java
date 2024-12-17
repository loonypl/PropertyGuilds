package amao.krzysek.propertyguilds.mysql;

import amao.krzysek.propertyguilds.PropertyGuilds;
import org.bukkit.Bukkit;

import java.sql.*;

public class MySQL {

    protected String host, port, user, password, database;
    protected Connection connection;
    protected Statement statement;

    public MySQL(String host, String port, String user, String password, String database) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    public MySQL() {}

    public String getDatabase() {
        return PropertyGuilds.getInstance().getConfig().getString("mysql.database");
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean openConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&user=" + user + "&password=" + password);
            Bukkit.getLogger().info("Successfully connected to MySQL database !");
            return true;
        } catch (SQLException sqlexception) {
            sqlexception.printStackTrace();
            return false;
        } catch (ClassNotFoundException classnotfound) {
            classnotfound.printStackTrace();
            return false;
        }
    }

    public void createTables() {
        try {
            try {
                statement = connection.createStatement();
                String sql = "CREATE TABLE Guilds (" +
                        " tag VARCHAR(255) NOT NULL PRIMARY KEY," +
                        " name VARCHAR(255) NOT NULL," +
                        " leader VARCHAR(255) NOT NULL," +
                        " members INTEGER NOT NULL," +
                        " members_list VARCHAR(255)," +
                        " alliances INTEGER NOT NULL," +
                        " alliances_list VARCHAR(255)," +
                        " location VARCHAR(255) NOT NULL," +
                        " location_radius INTEGER NOT NULL," +
                        " points INTEGER NOT NULL" +
                        " )";
                statement.executeUpdate(sql);
                statement.close();
            } finally {
                statement = connection.createStatement();
                String sql = "CREATE TABLE Users (" +
                        " name VARCHAR(255) NOT NULL PRIMARY KEY," +
                        " points INTEGER NOT NULL," +
                        " kills INTEGER NOT NULL," +
                        " deaths INTEGER NOT NULL," +
                        " guild VARCHAR(255)" +
                        " )";
                statement.executeUpdate(sql);
                statement.close();
                Bukkit.getLogger().info("Created tables in MySQL database !");
            }
        } catch (SQLException e) {}
    }

    public boolean closeConnection() {
        try {
            if(!(statement.isClosed())) statement.close();
            if(!(connection.isClosed())) connection.close();
            Bukkit.getLogger().info("Successfully disconnected from MySQL database !");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int executeUpdate(String update) {
        try {
            return statement.executeUpdate(update);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
