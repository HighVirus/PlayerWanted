package it.ivirus.playerwanted;

import it.ivirus.playerwanted.command.wantedcommand.WantedCommandHandler;
import it.ivirus.playerwanted.command.wantedcommand.WantedCommandTabCompleter;
import it.ivirus.playerwanted.data.WantedData;
import it.ivirus.playerwanted.database.SQLite;
import it.ivirus.playerwanted.database.SqlManager;
import it.ivirus.playerwanted.database.remote.MySQL;
import it.ivirus.playerwanted.listener.PlayerListener;
import it.ivirus.playerwanted.util.WantedUtil;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.concurrent.Executor;

@Getter
public class PlayerWantedMain extends JavaPlugin {

    @Getter
    private static PlayerWantedMain instance;

    private SqlManager sql;
    private File langFile;
    private FileConfiguration langConfig;
    private Economy economy;
    private BukkitAudiences adventure;

    private final Executor executor = runnable -> Bukkit.getScheduler().runTaskAsynchronously(this, runnable);

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.createLangFile("en_US", "it_IT");
        this.loadLangConfig();
        this.adventure = BukkitAudiences.create(this);
        WantedUtil.loadListeners(this, new PlayerListener(this));
        getCommand("wanted").setExecutor(new WantedCommandHandler(this));
        getCommand("wanted").setTabCompleter(new WantedCommandTabCompleter(this));
        if (getConfig().getBoolean("reward")) {
            if (!setupEconomy()) {
                getLogger().severe("Reward is enabled so Vault is required.");
                getLogger().severe("Disabled. Vault dependency not found!");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        this.setupDb();
        WantedData.getInstance().loadWantedPlayers();

        int pluginId = 13716;
        new Metrics(this, pluginId);

        getLogger().info("---------------------------------------");
        getLogger().info("PlayerWanted by iVirus_");
        getLogger().info("Version: " + this.getDescription().getVersion());
        getLogger().info("Discord support: https://discord.io/hoxija");
        getLogger().info("Telegram channel: https://t.me/HoxijaChannel");
        getLogger().info("Plugin is ready!");
        getLogger().info("---------------------------------------");
    }

    @Override
    public void onDisable() {
        if (sql instanceof MySQL) {
            MySQL mysql = (MySQL) sql;
            mysql.closePool();
        }
    }

    private void setupDb() {
        String database = getConfig().getString("MySQL.Database");
        String username = getConfig().getString("MySQL.Username");
        String password = getConfig().getString("MySQL.Password");
        String host = getConfig().getString("MySQL.Host");
        int port = getConfig().getInt("MySQL.Port");
        boolean ssl = getConfig().getBoolean("MySQL.SSL");
        sql = getConfig().getBoolean("MySQL.Enable") ? new MySQL(this, database, username, password, host, port, ssl) : new SQLite(this);
        try {
            sql.setup();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void createLangFile(String... names) {
        for (String name : names) {
            if (!new File(getDataFolder(), "languages" + File.separator + name + ".yml").exists()) {
                saveResource("languages" + File.separator + name + ".yml", false);
            }
        }
    }

    public void loadLangConfig() {
        langFile = new File(getDataFolder(), "languages" + File.separator + getConfig().getString("lang") + ".yml");
        if (!langFile.exists()) {
            langFile = new File(getDataFolder(), "languages" + File.separator + "en_US.yml");
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}
