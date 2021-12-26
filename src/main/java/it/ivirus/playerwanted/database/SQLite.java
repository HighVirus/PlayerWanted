package it.ivirus.playerwanted.database;

import it.ivirus.playerwanted.PlayerWantedMain;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLite extends SqlManager {

    public SQLite(PlayerWantedMain plugin) {
        super(plugin);
    }

    @Override
    public Connection getJdbcUrl() throws SQLException {
        String SQLITE_FILE_NAME = "database.db";
        String dataUrl = plugin.getDataFolder() + File.separator + "data" + File.separator + SQLITE_FILE_NAME;
        String url = "jdbc:sqlite:" + dataUrl;
        File dataFolder = new File(dataUrl);
        if (!dataFolder.exists())
            dataFolder.getParentFile().mkdirs();
        return DriverManager.getConnection(url);
    }

    @Override
    public void createTables() throws SQLException {
        PreparedStatement data = getConnection().prepareStatement("create TABLE if not exists `" + super.TABLE_WANTED_PLAYER + "` " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, AccountID VARCHAR(100), PlayerName VARCHAR(100), Reason VARCHAR(100), HeadMoney DOUBLE, Date DATETIME NOT NULL)");
        data.executeUpdate();
    }

}
