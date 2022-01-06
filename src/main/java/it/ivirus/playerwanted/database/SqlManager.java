package it.ivirus.playerwanted.database;

import it.ivirus.playerwanted.PlayerWantedMain;
import it.ivirus.playerwanted.util.PlayerWanted;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class SqlManager {
    protected final String TABLE_WANTED_PLAYER = "WantedPlayer";
    protected final PlayerWantedMain plugin;
    @Getter
    private Connection connection;

    public SqlManager(PlayerWantedMain plugin) {
        this.plugin = plugin;
    }

    public abstract void createTables() throws SQLException;

    public void setup() throws SQLException {
        try {
            if (getConnection() != null && !getConnection().isClosed()) {
                return;
            }
            setConnection(getJdbcUrl());
            this.createTables();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    protected abstract Connection getJdbcUrl() throws SQLException, ClassNotFoundException;

    public void addWantedPlayer(String playerUUID, String playerName, String submitterUUID, String reason, double headMoney, Date registrationDate) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TABLE_WANTED_PLAYER + " (AccountID, PlayerName, SubmitterID, Reason, Reward, Date)" +
                " values (?,?,?,?,?,?)")) {
            statement.setString(1, playerUUID);
            statement.setString(2, playerName);
            statement.setString(3, submitterUUID);
            statement.setString(4, reason);
            statement.setDouble(5, headMoney);
            statement.setDate(6, registrationDate);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removeWantedPlayer(String playerUUID) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + TABLE_WANTED_PLAYER + " WHERE AccountID=?")) {
            statement.setString(1, playerUUID);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public CompletableFuture<List<PlayerWanted>> getWantedList() {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE_WANTED_PLAYER + " ORDER BY Date DESC")) {
                List<PlayerWanted> playerWantedList = new ArrayList<>();
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    playerWantedList.add(new PlayerWanted(UUID.fromString(resultSet.getString("AccountID")), resultSet.getString("PlayerName"), UUID.fromString(resultSet.getString("SubmitterID")), resultSet.getString("Reason"), resultSet.getDouble("Reward"), resultSet.getDate("Date")));
                }
                return playerWantedList;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }, plugin.getExecutor());
    }
}
