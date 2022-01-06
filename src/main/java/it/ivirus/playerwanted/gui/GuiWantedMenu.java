package it.ivirus.playerwanted.gui;


import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import it.ivirus.playerwanted.PlayerWantedMain;
import it.ivirus.playerwanted.data.WantedData;
import it.ivirus.playerwanted.util.PlayerWanted;
import it.ivirus.playerwanted.util.Strings;
import it.ivirus.playerwanted.util.WantedUtil;
import lombok.Getter;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiWantedMenu {
    private final PlayerWantedMain plugin = PlayerWantedMain.getInstance();
    private final FileConfiguration config = plugin.getConfig();
    private final FileConfiguration langConfig = plugin.getLangConfig();
    @Getter
    private final PaginatedGui paginatedGui;

    public GuiWantedMenu() {
        paginatedGui = Gui.paginated()
                .title(Strings.getFormattedString(PlayerWantedMain.getInstance().getLangConfig().getString("gui.wanted.title")))
                .rows(6)
                .pageSize(36)
                .disableAllInteractions()
                .create();
        this.init();
    }

    public void init() {
        for (int i = 0; i < WantedData.getInstance().getPlayerWantedList().size(); i++)
            paginatedGui.addItem(ItemBuilder.from(getHeadWantedPlayer(WantedData.getInstance().getPlayerWantedList().get(i))).asGuiItem());

        paginatedGui.getFiller().fillBetweenPoints(5, 1, 6, 9, ItemBuilder.from(this.fillerItem()).asGuiItem());
        paginatedGui.setItem(6, 3, ItemBuilder.from(this.previousPageSlot()).asGuiItem(event -> paginatedGui.previous()));
        paginatedGui.setItem(6, 5, ItemBuilder.from(this.currentPageSlot(paginatedGui.getCurrentPageNum() - 1)).asGuiItem());
        paginatedGui.setItem(6, 7, ItemBuilder.from(this.nextPageSlot()).asGuiItem(event -> paginatedGui.next()));
    }


    private ItemStack nextPageSlot() {
        Material material = Material.matchMaterial(langConfig.getString("gui.wanted.next-button.item"));
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Strings.getOldFormatString(LegacyComponentSerializer.legacyAmpersand().serialize(Strings.getFormattedString(langConfig.getString("gui.wanted.next-button.name")))));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack previousPageSlot() {
        Material material = Material.matchMaterial(langConfig.getString("gui.wanted.previous-button.item"));
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Strings.getOldFormatString(LegacyComponentSerializer.legacyAmpersand().serialize(Strings.getFormattedString(langConfig.getString("gui.wanted.previous-button.name")))));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack currentPageSlot(int page) {
        Material material = Material.matchMaterial(langConfig.getString("gui.wanted.current-page-button.item"));
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Strings.getOldFormatString(LegacyComponentSerializer.legacyAmpersand().serialize(Strings.getFormattedString(langConfig.getString("gui.wanted.current-page-button.name")
                .replaceAll("%current_page%", String.valueOf(page))))));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack fillerItem() {
        Material material = Material.matchMaterial(langConfig.getString("gui.wanted.filler.item"));
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Strings.getOldFormatString(LegacyComponentSerializer.legacyAmpersand().serialize(Strings.getFormattedString(langConfig.getString("gui.wanted.filler.name")))));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack getHeadWantedPlayer(PlayerWanted playerWanted) {
        ItemStack itemStack = WantedUtil.getHeadItemByUUID(playerWanted.getPlayerUUID());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Strings.getOldFormatString(LegacyComponentSerializer.legacyAmpersand().serialize(Strings.getFormattedString(langConfig.getString("gui.wanted.wanted-account.name")
                .replaceAll("%player_name%", playerWanted.getPlayerName())))));
        List<String> loreEmptySlot = new ArrayList<>();
        OfflinePlayer submitter = Bukkit.getOfflinePlayer(playerWanted.getPlayerSubmitter());
        if (playerWanted.getReward() != 0) {
            for (String s : langConfig.getStringList("gui.wanted.wanted-account.lore-with-reward")) {
                loreEmptySlot.add(Strings.getOldFormatString(LegacyComponentSerializer.legacyAmpersand().serialize(Strings.getFormattedString(s
                        .replaceAll("%player_name%", playerWanted.getPlayerName())
                        .replaceAll("%reason%", playerWanted.getReason())
                        .replaceAll("%reward%", String.format("%.2f", playerWanted.getReward()))
                        .replaceAll("%insert_date%", WantedUtil.dateFormat.format(playerWanted.getDate()))
                        .replaceAll("%submitter%", submitter.getName())
                ))));
            }
        } else {
            for (String s : langConfig.getStringList("gui.wanted.wanted-account.lore-without-reward")) {
                loreEmptySlot.add(Strings.getOldFormatString(LegacyComponentSerializer.legacyAmpersand().serialize(Strings.getFormattedString(s
                        .replaceAll("%player_name%", playerWanted.getPlayerName())
                        .replaceAll("%reason%", playerWanted.getReason())
                        .replaceAll("%insert_date%", WantedUtil.dateFormat.format(playerWanted.getDate()))
                        .replaceAll("%submitter%", submitter.getName())
                ))));
            }
        }
        itemMeta.setLore(loreEmptySlot);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
