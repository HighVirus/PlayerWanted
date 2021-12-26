package it.ivirus.playerwanted.util;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class WantedUtil {
    public static void loadListeners(JavaPlugin plugin, Listener... listeners) {
        for (Listener l : listeners) {
            Bukkit.getPluginManager().registerEvents(l, plugin);
        }
    }

    public static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static ItemStack getHeadItemByUUID(UUID uuid) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta headmeta = (SkullMeta) head.getItemMeta();

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        headmeta.setOwningPlayer(offlinePlayer);
        head.setItemMeta(headmeta);
        return head;
    }
}
