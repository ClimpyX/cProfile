package com.climpy.profile.commands;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.inv.ClickableItem;
import com.climpy.profile.inv.SmartInventory;
import com.climpy.profile.inv.content.InventoryContents;
import com.climpy.profile.inv.content.InventoryProvider;
import com.climpy.profile.user.User;
import com.climpy.profile.utils.ItemUtils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;

public class UserMenu implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("userMenu")
            .provider(new UserMenu())
            .size(6, 9)
            .title("§8§l» §8Kullanıcı Verileri")
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getUniqueId());
        contents.set(1, 4, ClickableItem.empty(ItemUtils.createSkullItem(user.getRankType().getPrefix() + player.getName(), 1, (short) 3, player.getName() ,Arrays.asList(" ", "§eÇevrim içi: " + (user.isOnlineStatus() ? "a" : "b"), "" + user.isOnlineStatus()))));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(ItemUtils.createItem(Material.STAINED_GLASS_PANE, "§7Kullanıcı Verileri görüntüleniyor..", 1, DyeColor.BLACK.getData())));
        User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getUniqueId());

        contents.set(3, 3, ClickableItem.of(ItemUtils.createItem(Material.SIGN, "§b§lRütbe Bilgileri",  Arrays.asList("", "§e▪ Rütbe: §f" + user.getRankType(), "§e▪ Görünen İsim: §f" + user.getRankType().getDisplayName(), "", "§e▪ Prefix: §f" + user.getRankType().getPrefix(), "§e▪ Suffix: §f" + user.getRankType().getSuffix())), e -> {

        }));

        contents.set(3, 4, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§b§lSembol Bilgileri",  Arrays.asList("", "§e▪ Sembol: §f" + user.getSymbolType().getDisplayName(), "§e▪ Sembol Görünümü: §f" + user.getSymbolType().getDisplayName())), e -> {

        }));

        contents.set(3, 5, ClickableItem.of(ItemUtils.createItem(Material.GOLD_INGOT, "§b§lKredi Bilgileri",  Arrays.asList("", "§e▪ Toplam Kredi: §f" + user.getCredit())), e -> {

        }));

    }
}
