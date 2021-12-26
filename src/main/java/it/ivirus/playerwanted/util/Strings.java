package it.ivirus.playerwanted.util;

import it.ivirus.playerwanted.PlayerWantedMain;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;

public enum Strings {

    PREFIX("prefix"),
    INFO_HELP_PLAYER("info.help"),
    INFO_HELP_ADMIN("info.help-admin"),
    INFO_RELOAD_EXECUTED("info.reload-executed"),
    INFO_WANTED_TARGET_ADDED("info.wanted-target-added"),
    INFO_WANTED_TARGET_REMOVED("info.wanted-target-removed"),
    INFO_PLAYER_WANTED_WITH_REWARD_MESSAGE("info.player-wanted-with-reward-wessage"),
    INFO_PLAYER_WANTED_WITHOUT_REWARD_MESSAGE("info.player-wanted-without-reward-wessage"),
    INFO_PLAYER_WANTED_TITLE("info.player-wanted-title"),
    INFO_PLAYER_NOT_WANTED_MESSAGE("info.player-not-wanted-message"),
    INFO_PLAYER_NOT_WANTED_TITLE("info.player-not-wanted-title"),
    INFO_LIST_HEADER("info.list.header"),
    INFO_REWARD_MESSAGE("info.reward-message"),
    ERROR_ADD_WITH_REWARD_USAGE("errors.add-usage-with-reward"),
    ERROR_ADD_WITHOUT_REWARD_USAGE("errors.add-usage-without-reward"),
    ERROR_REMOVE_USAGE("errors.remove-usage"),
    ERROR_ONLY_PLAYER("errors.only-player"),
    ERROR_INVALID_VALUE("errors.invalid-value"),
    ERROR_TARGET_OFFLINE("errors.target-offline"),
    ERROR_TARGET_CANNOT_SENDER("errors.target-cannot-sender"),
    ERROR_TARGET_ALREADY_WANTED("errors.target-already-wanted"),
    ERROR_NO_PERMISSION("errors.no-permission");


    private final PlayerWantedMain plugin = PlayerWantedMain.getInstance();
    @Getter
    private final String path;

    Strings(String path) {
        this.path = path;
    }

    private String getPrefix() {
        return plugin.getLangConfig().getString(PREFIX.getPath());
    }

    private static String getStaticPrefix() {
        return PlayerWantedMain.getInstance().getLangConfig().getString(PREFIX.getPath());
    }

    public String getString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (plugin.getLangConfig().isList(path)) {
            for (int i = 0; i < plugin.getLangConfig().getStringList(path).size(); i++) {
                if (i == PlayerWantedMain.getInstance().getLangConfig().getStringList(path).size() - 1) {
                    stringBuilder.append(PlayerWantedMain.getInstance().getLangConfig().getStringList(path).get(i));
                } else {
                    stringBuilder.append(PlayerWantedMain.getInstance().getLangConfig().getStringList(path).get(i) + "\n");
                }
            }
        } else {
            return plugin.getLangConfig().getString(path).replaceAll("%prefix%", getPrefix());
        }
        return stringBuilder.toString().replaceAll("%prefix%", getPrefix());
    }

    public static String getString(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        if (PlayerWantedMain.getInstance().getLangConfig().isList(path)) {
            for (int i = 0; i < PlayerWantedMain.getInstance().getLangConfig().getStringList(path).size(); i++) {
                if (i == PlayerWantedMain.getInstance().getLangConfig().getStringList(path).size() - 1) {
                    stringBuilder.append(PlayerWantedMain.getInstance().getLangConfig().getStringList(path).get(i));
                } else {
                    stringBuilder.append(PlayerWantedMain.getInstance().getLangConfig().getStringList(path).get(i) + "\n");
                }
            }
        } else {
            return PlayerWantedMain.getInstance().getLangConfig().getString(path).replaceAll("%prefix%", Strings.getStaticPrefix());
        }
        return stringBuilder.toString().replaceAll("%prefix%", Strings.getStaticPrefix());
    }

    public Component getFormattedString() {
        if (getString().contains("&"))
            return LegacyComponentSerializer.legacyAmpersand().deserialize(getString());
        else
            return MiniMessage.get().parse(getString());
    }

    public static Component getFormattedString(String string) {
        if (string.contains("&"))
            return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
        else
            return MiniMessage.get().parse(string);
    }

    public static String getOldFormatString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
