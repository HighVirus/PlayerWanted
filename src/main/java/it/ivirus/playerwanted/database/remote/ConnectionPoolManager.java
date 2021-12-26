package it.ivirus.playerwanted.database.remote;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import it.ivirus.playerwanted.PlayerWantedMain;
import it.ivirus.playerwanted.database.SqlManager;
import lombok.Getter;
import lombok.Setter;

public abstract class ConnectionPoolManager extends SqlManager {
    @Getter
    @Setter
    private HikariDataSource dataSource;

    @Getter
    private final HikariConfig hikariConfig = new HikariConfig();

    public ConnectionPoolManager(PlayerWantedMain plugin) {
        super(plugin);
        setupPool();
    }

    private void setupPool() {
        hikariConfig.setConnectionTimeout(30000);
    }

    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

}
