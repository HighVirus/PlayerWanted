package it.ivirus.playerwanted.command.wantedcommand;

import it.ivirus.playerwanted.PlayerWantedMain;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class WantedCommandTabCompleter implements TabCompleter {
    private final PlayerWantedMain plugin;

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        CommandExecutor commandExecutor = plugin.getCommand(command.getName()).getExecutor();
        WantedCommandHandler commandHandler = (WantedCommandHandler) commandExecutor;
        if (args.length == 1) {
            return new ArrayList<>(commandHandler.getCommands().keySet());
        }

        return null;
    }
}
