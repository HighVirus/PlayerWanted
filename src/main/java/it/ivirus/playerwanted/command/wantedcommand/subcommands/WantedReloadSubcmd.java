package it.ivirus.playerwanted.command.wantedcommand.subcommands;

import it.ivirus.playerwanted.command.SubCommand;
import it.ivirus.playerwanted.command.wantedcommand.WantedCommandHandler;
import it.ivirus.playerwanted.util.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WantedReloadSubcmd extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (!sender.hasPermission("playerwanted.admin")){
            adventure.sender(sender).sendMessage(Strings.ERROR_NO_PERMISSION.getFormattedString());
            return;
        }
        plugin.reloadConfig();
        plugin.loadLangConfig();
        plugin.getCommand("wanted").setExecutor(new WantedCommandHandler(plugin));
        adventure.sender(sender).sendMessage(Strings.INFO_RELOAD_EXECUTED.getFormattedString());
    }
}
