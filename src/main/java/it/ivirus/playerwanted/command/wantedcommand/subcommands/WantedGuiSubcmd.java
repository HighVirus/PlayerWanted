package it.ivirus.playerwanted.command.wantedcommand.subcommands;

import it.ivirus.playerwanted.command.SubCommand;
import it.ivirus.playerwanted.gui.GuiWantedMenu;
import it.ivirus.playerwanted.util.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WantedGuiSubcmd extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (!sender.hasPermission("playerwanted.gui")) {
            adventure.sender(sender).sendMessage(Strings.ERROR_NO_PERMISSION.getFormattedString());
            return;
        }
        if (!(sender instanceof Player)) {
            adventure.sender(sender).sendMessage(Strings.ERROR_ONLY_PLAYER.getFormattedString());
            return;
        }
        Player player = (Player) sender;
        new GuiWantedMenu().getPaginatedGui().open(player);
    }
}
