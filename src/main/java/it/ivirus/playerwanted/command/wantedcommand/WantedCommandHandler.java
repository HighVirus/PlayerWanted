package it.ivirus.playerwanted.command.wantedcommand;

import it.ivirus.playerwanted.PlayerWantedMain;
import it.ivirus.playerwanted.command.SubCommand;
import it.ivirus.playerwanted.command.wantedcommand.subcommands.*;
import it.ivirus.playerwanted.data.WantedData;
import it.ivirus.playerwanted.util.Strings;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class WantedCommandHandler implements CommandExecutor {
    private final PlayerWantedMain plugin;
    private final WantedData wantedData = WantedData.getInstance();
    private final FileConfiguration langConfig;

    public WantedCommandHandler(PlayerWantedMain plugin) {
        this.plugin = plugin;
        this.langConfig = plugin.getLangConfig();
        registerCommand("reload", new WantedReloadSubcmd());

        if (plugin.getConfig().getBoolean("reward")) {
            registerCommand("add", new WantedAddWithRewardSubcmd());
        } else {
            registerCommand("add", new WantedAddWithoutRewardSubcmd());
        }
        registerCommand("remove", new WantedRemoveSubcmd());
        registerCommand("help", new WantedHelpSubcmd());
        registerCommand("gui", new WantedGuiSubcmd());
        registerCommand("list", new WantedListSubcmd());
    }

    @Getter
    private final Map<String, SubCommand> commands = new HashMap<>();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) {

        if (args.length == 0) {
            plugin.getAdventure().sender(sender).sendMessage(Strings.getFormattedString("&3-------------------------------"));
            plugin.getAdventure().sender(sender).sendMessage(Strings.getFormattedString("&3Plugin developer: &fiVirus_"));
            plugin.getAdventure().sender(sender).sendMessage(Strings.getFormattedString("&3Telegram: &fhttps://t.me/HoxijaChannel"));
            plugin.getAdventure().sender(sender).sendMessage(Strings.getFormattedString("&3Discord: &fhttps://discord.io/hoxija"));
            plugin.getAdventure().sender(sender).sendMessage(Strings.getFormattedString("&3-------------------------------"));
            return true;
        }

        if (!commands.containsKey(args[0].toLowerCase())) {
            plugin.getAdventure().sender(sender).sendMessage(Strings.INFO_HELP_PLAYER.getFormattedString());
            if (sender.hasPermission("playerwanted.admin"))
                plugin.getAdventure().sender(sender).sendMessage(Strings.INFO_HELP_ADMIN.getFormattedString());
            return true;
        }

        commands.get(args[0].toLowerCase()).onCommand(sender, command, args);
        return true;
    }

    private void registerCommand(String cmd, SubCommand subCommand) {
        commands.put(cmd, subCommand);
    }
}
