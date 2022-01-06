package it.ivirus.playerwanted.listener;

import it.ivirus.playerwanted.PlayerWantedMain;
import it.ivirus.playerwanted.data.WantedData;
import it.ivirus.playerwanted.util.PlayerWanted;
import it.ivirus.playerwanted.util.Strings;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    private final PlayerWantedMain plugin;

    public PlayerListener(PlayerWantedMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (WantedData.getInstance().getPlayerWantedMap().containsKey(player.getUniqueId())) {
            player.sendTitle(Strings.getOldFormatString(LegacyComponentSerializer.legacyAmpersand().serialize(Strings.INFO_PLAYER_WANTED_TITLE.getFormattedString())), " ", 10, 40, 20);
        }
    }

    @EventHandler
    public void onPlayerKillTarget(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (!plugin.getConfig().getBoolean("reward")) return;

        Player killer = (Player) event.getDamager();
        OfflinePlayer offlineKiller = Bukkit.getOfflinePlayer(killer.getUniqueId());
        Player player = (Player) event.getEntity();

        if (WantedData.getInstance().getPlayerWantedMap().containsKey(player.getUniqueId())) {
            PlayerWanted playerWanted = WantedData.getInstance().getPlayerWantedMap().get(player.getUniqueId());
            if ((player.getHealth() - event.getDamage()) <= 0) {
                plugin.getEconomy().depositPlayer(offlineKiller, playerWanted.getReward());
                plugin.getAdventure().player(killer).sendMessage(Strings.getFormattedString(Strings.INFO_REWARD_MESSAGE.getString()
                        .replaceAll("%target_name%", player.getName())
                        .replaceAll("%reward%", String.format("%.2f", playerWanted.getReward()))
                ));
                plugin.getSql().removeWantedPlayer(player.getUniqueId().toString());
                WantedData.getInstance().getPlayerWantedMap().remove(player.getUniqueId());
                WantedData.getInstance().getPlayerWantedList().remove(playerWanted);
            }
        }
    }
}
