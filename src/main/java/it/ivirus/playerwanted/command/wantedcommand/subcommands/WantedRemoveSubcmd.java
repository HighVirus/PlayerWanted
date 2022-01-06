package it.ivirus.playerwanted.command.wantedcommand.subcommands;

import it.ivirus.playerwanted.command.SubCommand;
import it.ivirus.playerwanted.data.WantedData;
import it.ivirus.playerwanted.util.PlayerWanted;
import it.ivirus.playerwanted.util.Strings;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WantedRemoveSubcmd extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (!sender.hasPermission("playerwanted.remove")) {
            adventure.sender(sender).sendMessage(Strings.ERROR_NO_PERMISSION.getFormattedString());
            return;
        }
        if (!(sender instanceof Player)) {
            adventure.sender(sender).sendMessage(Strings.ERROR_ONLY_PLAYER.getFormattedString());
            return;
        }
        if (args.length != 2) {
            adventure.sender(sender).sendMessage(Strings.ERROR_REMOVE_USAGE.getFormattedString());
            return;
        }
        Player player = (Player) sender;
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (player.equals(target)) {
            adventure.player(player).sendMessage(Strings.ERROR_TARGET_CANNOT_SENDER.getFormattedString());
            return;
        }
        PlayerWanted targetWanted = WantedData.getInstance().getPlayerWantedMap().get(target.getUniqueId());

        double money = targetWanted.getReward();

        OfflinePlayer playerSubmitter = Bukkit.getOfflinePlayer(targetWanted.getPlayerSubmitter());
        plugin.getEconomy().depositPlayer(playerSubmitter, money);

        WantedData.getInstance().getPlayerWantedList().remove(targetWanted);
        WantedData.getInstance().getPlayerWantedMap().remove(target.getUniqueId());
        plugin.getSql().removeWantedPlayer(target.getUniqueId().toString());
        if (money == 0){
            adventure.player(player).sendMessage(Strings.getFormattedString(Strings.INFO_WANTED_TARGET_REMOVED_WITHOUT_REWARD.getString()
                    .replaceAll("%target_name%", target.getName())));
        } else {
            adventure.player(player).sendMessage(Strings.getFormattedString(Strings.INFO_WANTED_TARGET_REMOVED_WITH_REWARD.getString()
                    .replaceAll("%target_name%", target.getName())
                    .replaceAll("%reward%", String.format("%.2f", money))
            ));
        }
        if (target.isOnline()) {
            Player onlineTarget = target.getPlayer();
            adventure.player(onlineTarget).sendMessage(Strings.INFO_PLAYER_NOT_WANTED_MESSAGE.getFormattedString());
            onlineTarget.sendTitle(Strings.getOldFormatString(LegacyComponentSerializer.legacyAmpersand().serialize(Strings.INFO_PLAYER_NOT_WANTED_TITLE.getFormattedString())), " ", 10, 40, 20);
        }

    }
}
