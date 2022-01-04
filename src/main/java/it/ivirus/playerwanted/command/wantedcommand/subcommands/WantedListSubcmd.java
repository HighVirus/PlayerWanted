package it.ivirus.playerwanted.command.wantedcommand.subcommands;

import it.ivirus.playerwanted.command.SubCommand;
import it.ivirus.playerwanted.data.WantedData;
import it.ivirus.playerwanted.util.PlayerWanted;
import it.ivirus.playerwanted.util.Strings;
import it.ivirus.playerwanted.util.WantedUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WantedListSubcmd extends SubCommand {
    int page = 0;

    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (!sender.hasPermission("playerwanted.list")) {
            adventure.sender(sender).sendMessage(Strings.ERROR_NO_PERMISSION.getFormattedString());
            return;
        }
        if (args.length == 2){
            if (!NumberUtils.isDigits(args[1])) {
                adventure.sender(sender).sendMessage(Strings.ERROR_INVALID_VALUE.getFormattedString());
                return;
            }
            this.page = Integer.parseInt(args[1]);
            if (this.page < 0) {
                adventure.sender(sender).sendMessage(Strings.ERROR_INVALID_VALUE.getFormattedString());
                return;
            }
        }
        if (!(sender instanceof Player)) {
            adventure.sender(sender).sendMessage(Strings.ERROR_ONLY_PLAYER.getFormattedString());
            return;
        }
        adventure.sender(sender).sendMessage(Strings.getFormattedString(Strings.INFO_LIST_HEADER.getString()
                .replaceAll("%page%", String.valueOf(page))));
        this.sendWantedList(sender);
    }

    private void sendWantedList(CommandSender sender) {
        int index = indexToStart(this.page);
        int indexLimit = 26;
        if ((index + 27) > WantedData.getInstance().getPlayerWantedList().size()) {
            indexLimit = WantedData.getInstance().getPlayerWantedList().size();
        } else {
            indexLimit = indexLimit + 27;
        }
        for (int i = index; i < indexLimit; i++) {
            PlayerWanted playerWanted = WantedData.getInstance().getPlayerWantedList().get(i);
            if (playerWanted.getReward() != 0) {
                adventure.sender(sender).sendMessage(Strings.getFormattedString(Strings.getString("info.list.list-format-with-reward")
                        .replaceAll("%player_name%", playerWanted.getPlayerName())
                        .replaceAll("%reward%", String.format("%.2f", playerWanted.getReward()))
                        .replaceAll("%insert_date%", WantedUtil.dateFormat.format(playerWanted.getDate()))
                        .replaceAll("%reason%", playerWanted.getReason())
                ));
            } else {
                adventure.sender(sender).sendMessage(Strings.getFormattedString(Strings.getString("info.list.list-format-without-reward")
                        .replaceAll("%player_name%", playerWanted.getPlayerName())
                        .replaceAll("%insert_date%", WantedUtil.dateFormat.format(playerWanted.getDate()))
                        .replaceAll("%reason%", playerWanted.getReason())
                ));
            }
        }
    }

    private int indexToStart(int page) {
        return page * 27;
    }
}
