package it.ivirus.playerwanted.database.remote;

import com.zaxxer.hikari.HikariDataSource;
import it.ivirus.playerwanted.PlayerWantedMain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQL extends ConnectionPoolManager {
    private final String database, username, password, host;
    private final boolean ssl;
    private final int port;

    public MySQL(PlayerWantedMain plugin, String database, String username, String password, String host, int port, boolean ssl) {
        super(plugin);
        this.database = database;
        this.host = host;
        this.password = password;
        this.username = username;
        this.port = port;
        this.ssl = ssl;
    }

    @Override
    public Connection getJdbcUrl() throws SQLException {
        getHikariConfig().setJdbcUrl(
                "jdbc:mysql://" +
                        this.host +
                        ":" +
                        this.port +
                        "/" +
                        this.database +
                        "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=" +
                        this.ssl
        );
        getHikariConfig().setUsername(this.username);
        getHikariConfig().setPassword(this.password);
        setDataSource(new HikariDataSource(getHikariConfig()));

        return getDataSource().getConnection();
    }

    @Override
    public void createTables() throws SQLException {
        PreparedStatement data = getConnection().prepareStatement("create TABLE if not exists `" + super.TABLE_WANTED_PLAYER + "` " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, AccountID VARCHAR(100), PlayerName VARCHAR(100), SubmitterID VARCHAR(100), Reason VARCHAR(100), Reward DOUBLE, Date DATETIME NOT NULL)");
        data.executeUpdate();
    }

}
