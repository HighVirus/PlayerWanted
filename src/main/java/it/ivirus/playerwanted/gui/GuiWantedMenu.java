package it.ivirus.playerwanted.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import it.ivirus.playerwanted.PlayerWantedMain;
import it.ivirus.playerwanted.data.WantedData;
import it.ivirus.playerwanted.util.PlayerWanted;
import it.ivirus.playerwanted.util.Strings;
import it.ivirus.playerwanted.util.WantedUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiWantedMenu implements InventoryProvider {
    private final PlayerWantedMain plugin = PlayerWantedMain.getInstance();
    private final FileConfiguration config = plugin.getConfig();
    private final FileConfiguration langConfig = plugin.getLangConfig();

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("WantedInv")
            .provider(new GuiWantedMenu())
            .size(6, 9)
            .title(Strings.getOldFormatString(LegacyComponentSerializer.legacyAmpersand().serialize(Strings.getFormattedString(PlayerWantedMain.getInstance().getLangConfig().getString("gui.wanted.title")))))
            .manager(PlayerWantedMain.getInvManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        int listSize = WantedData.getInstance().getPlayerWantedList().size();
        ClickableItem[] items = new ClickableItem[listSize];
        for (int i = 0; i < WantedData.getInstance().getPlayerWantedList().size(); i++)
            items[i] = ClickableItem.empty(getHeadWantedPlayer(WantedData.getInstance().getPlayerWantedList().get(i)));

        pagination.setItems(items);
        pagination.setItemsPerPage(27);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        contents.set(4, 2, ClickableItem.of(this.previousPageSlot(),
                e -> INVENTORY.open(player, pagination.previous().getPage())));
        contents.set(4, 4, ClickableItem.empty(this.currentPageSlot(pagination.getPage())));
        contents.set(4, 6, ClickableItem.of(this.nextPageSlot(),
                e -> INVENTORY.open(player, pagination.next().getPage())));
        this.fill(contents, ClickableItem.empty(this.fillerItem()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

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

    private void fill(InventoryContents contents, ClickableItem clickableItem) {
        for (int row = 0; row < contents.all().length; row++) {
            for (int column = 0; column < contents.all()[row].length; column++) {
                if (contents.get(row, column).isEmpty() || contents.get(row, column).get().getItem().getType() == Material.AIR)
                    contents.set(row, column, clickableItem);
            }
        }
    }

    private ItemStack getHeadWantedPlayer(PlayerWanted playerWanted) {
        ItemStack itemStack = WantedUtil.getHeadItemByUUID(playerWanted.getPlayerUUID());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Strings.getOldFormatString(LegacyComponentSerializer.legacyAmpersand().serialize(Strings.getFormattedString(langConfig.getString("gui.wanted.wanted-account.name")
                .replaceAll("%player_name%", playerWanted.getPlayerName())))));
        List<String> loreEmptySlot = new ArrayList<>();
        if (playerWanted.getReward() != 0) {
            for (String s : langConfig.getStringList("gui.wanted.wanted-account.lore-with-reward")) {
                loreEmptySlot.add(Strings.getOldFormatString(LegacyComponentSerializer.legacyAmpersand().serialize(Strings.getFormattedString(s
                        .replaceAll("%player_name%", playerWanted.getPlayerName())
                        .replaceAll("%reason%", playerWanted.getReason())
                        .replaceAll("%reward%", String.format("%.2f", playerWanted.getReward()))
                        .replaceAll("%insert_date%", WantedUtil.dateFormat.format(playerWanted.getDate()))
                ))));
            }
        } else {
            for (String s : langConfig.getStringList("gui.wanted.wanted-account.lore-without-reward")) {
                loreEmptySlot.add(Strings.getOldFormatString(LegacyComponentSerializer.legacyAmpersand().serialize(Strings.getFormattedString(s
                        .replaceAll("%player_name%", playerWanted.getPlayerName())
                        .replaceAll("%reason%", playerWanted.getReason())
                        .replaceAll("%insert_date%", WantedUtil.dateFormat.format(playerWanted.getDate()))
                ))));
            }
        }
        itemMeta.setLore(loreEmptySlot);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
