package it.ivirus.playerwanted.command.wantedcommand.subcommands;

import it.ivirus.playerwanted.command.SubCommand;
import it.ivirus.playerwanted.data.WantedData;
import it.ivirus.playerwanted.util.PlayerWanted;
import it.ivirus.playerwanted.util.Strings;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Date;

public class WantedAddWithRewardSubcmd extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (!sender.hasPermission("playerwanted.add")) {
            adventure.sender(sender).sendMessage(Strings.ERROR_NO_PERMISSION.getFormattedString());
            return;
        }
        if (!(sender instanceof Player)) {
            adventure.sender(sender).sendMessage(Strings.ERROR_ONLY_PLAYER.getFormattedString());
            return;
        }
        if (args.length < 4) {
            adventure.sender(sender).sendMessage(Strings.ERROR_ADD_WITH_REWARD_USAGE.getFormattedString());
            return;
        }
        if (!NumberUtils.isNumber(args[2])) {
            adventure.sender(sender).sendMessage(Strings.ERROR_INVALID_VALUE.getFormattedString());
            return;
        }
        double value = Math.ceil(Double.parseDouble(args[2]));
        if (value < 0) {
            adventure.sender(sender).sendMessage(Strings.ERROR_INVALID_VALUE.getFormattedString());
            return;
        }
        StringBuilder reason = new StringBuilder();
        for (int i = 3; i < args.length; i++) {
            reason.append(" ").append(args[i]);
        }
        Player player = (Player) sender;
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        Date date = new Date(System.currentTimeMillis());
        if (player.equals(target)) {
            adventure.player(player).sendMessage(Strings.ERROR_TARGET_CANNOT_SENDER.getFormattedString());
            return;
        }
        if (WantedData.getInstance().getPlayerWantedMap().containsKey(target.getUniqueId())) {
            adventure.sender(sender).sendMessage(Strings.ERROR_TARGET_ALREADY_WANTED.getFormattedString());
            return;
        }
        PlayerWanted playerWanted = new PlayerWanted(target.getUniqueId(), target.getName(), reason.toString(), value, date);
        WantedData.getInstance().getPlayerWantedMap().put(target.getUniqueId(), playerWanted);
        WantedData.getInstance().getPlayerWantedList().add(playerWanted);
        plugin.getSql().addWantedPlayer(target.getUniqueId().toString(), target.getName(), reason.toString(), value, date);
        adventure.player(player).sendMessage(Strings.getFormattedString(Strings.INFO_WANTED_TARGET_ADDED.getString()
                .replaceAll("%target_name%", target.getName())));

        if (target.isOnline()) {
            Player onlineTarget = target.getPlayer();
            adventure.player(onlineTarget).sendMessage(Strings.getFormattedString(Strings.INFO_PLAYER_WANTED_WITH_REWARD_MESSAGE.getString()
                    .replaceAll("%reason%", reason.toString())
                    .replaceAll("%reward%", String.format("%.2f", value)
                    )));
            onlineTarget.sendTitle(Strings.getOldFormatString(LegacyComponentSerializer.legacyAmpersand().serialize(Strings.INFO_PLAYER_WANTED_TITLE.getFormattedString())), " ", 10, 40, 20);
        }


    }
}
