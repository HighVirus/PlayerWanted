package it.ivirus.playerwanted.command.wantedcommand.subcommands;

import it.ivirus.playerwanted.command.SubCommand;
import it.ivirus.playerwanted.util.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WantedHelpSubcmd extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player)) {
            adventure.sender(sender).sendMessage(Strings.ERROR_ONLY_PLAYER.getFormattedString());
            return;
        }

        adventure.sender(sender).sendMessage(Strings.INFO_HELP_PLAYER.getFormattedString());
        if (sender.hasPermission("playerwanted.admin")) {
            adventure.sender(sender).sendMessage(Strings.INFO_HELP_ADMIN.getFormattedString());
        }
    }
}
