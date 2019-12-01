package com.climpy.profile.symbol;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.inv.ClickableItem;
import com.climpy.profile.inv.SmartInventory;
import com.climpy.profile.inv.content.InventoryContents;
import com.climpy.profile.inv.content.InventoryProvider;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.profile.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Random;

public class SymbolMenu implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("symbolMenu")
            .provider(new SymbolMenu())
            .size(5, 9)
            .title("§8§l» §8Sembol Menüsü")
            .build();

    private final Random random = new Random();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        if(state % 5 != 0)
            return;

        short durability = (short) random.nextInt(15);

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, durability);
        contents.fillBorders(ClickableItem.empty(glass));

        final User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(player.getUniqueId());

        for (SymbolType symbolType : SymbolType.values()) {
            boolean nowSymbolUsableStatus =! (symbolType.getRankType() == targetUser.getRankType());
            boolean symbolActiveStatus =! (symbolType == targetUser.getSymbolType());
            final String[] symbolLore = new String[3];


            symbolLore[0] = (" ");
            symbolLore[1] = ChatColor.GRAY + "Kullanma İzni: " + (!nowSymbolUsableStatus ? (ChatColor.GREEN + "Açık") : (ChatColor.RED + "Kapalı"));
            symbolLore[2] = symbolActiveStatus ? (ChatColor.GRAY + "Sembol seçildi!") : (ChatColor.YELLOW + "Etkinleştirmek için sembole tıklayabilirsin!");

            contents.set(1, 1, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §7Sembol: §f§l★", Arrays.asList(new String[] { symbolLore[0], symbolLore[1], symbolLore[2] })), e -> {
                if (!targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.VIP)) {
                            player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.VIP.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                            return;
                        }

                        if(targetUser.getSymbolType() == SymbolType.SYMBOL_1){
                            player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                            return;
                        }

                        targetUser.setSymbolType(SymbolType.SYMBOL_1);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                        targetUser.save();
                    }));
            
            contents.set(1, 2, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §7Sembol: §f§l✿", Arrays.asList(new String[] { symbolLore[0], symbolLore[1], symbolLore[2] })), e -> {
                if(targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.VIPPLUS)) {
                    player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.VIPPLUS.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                    return;
                }

                if(targetUser.getSymbolType() == SymbolType.SYMBOL_2){
                    player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                    return;
                }

                targetUser.setSymbolType(SymbolType.SYMBOL_2);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                targetUser.save();
            }));

            contents.set(1, 3, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §f§l♫", Arrays.asList(new String[] { symbolLore[0], symbolLore[1], symbolLore[2] })), e -> {
                if(targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.VIPPLUS)) {
                    player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.VIPPLUS.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                    return;
                }

                if(targetUser.getSymbolType() == SymbolType.SYMBOL_3){
                    player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                    return;
                }

                targetUser.setSymbolType(SymbolType.SYMBOL_3);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                targetUser.save();
            }));

            contents.set(1, 4, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §d§l❥", Arrays.asList(symbolLore[0], symbolLore[1], symbolLore[2])), e -> {
                if(targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.VIPPLUS)) {
                    player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.VIPPLUS.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                    return;
                }

                if(targetUser.getSymbolType() == SymbolType.SYMBOL_4){
                    player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                    return;
                }

                targetUser.setSymbolType(SymbolType.SYMBOL_4);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                targetUser.save();
            }));

            contents.set(1, 5, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §d§l♚", Arrays.asList(new String[] { symbolLore[0], symbolLore[1], symbolLore[2] })), e -> {
                if(!targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.CVIP)) {
                    player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.CVIP.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                    return;
                }

                if(targetUser.getSymbolType() == SymbolType.SYMBOL_5){
                    player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                    return;
                }

                targetUser.setSymbolType(SymbolType.SYMBOL_5);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                targetUser.save();
            }));

            contents.set(1, 6, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §d§l☘", Arrays.asList(new String[] { symbolLore[0], symbolLore[1], symbolLore[2] })), e -> {
                if(targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.CVIP)) {
                    player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.CVIP.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                    return;
                }

                if(targetUser.getSymbolType() == SymbolType.SYMBOL_6){
                    player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                    return;
                }

                targetUser.setSymbolType(SymbolType.SYMBOL_6);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                targetUser.save();
            }));

            contents.set(1, 7, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §a§l✎", Arrays.asList(new String[] { symbolLore[0], symbolLore[1], symbolLore[2] })), e -> {
                if(targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.MVIP)) {
                    player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MVIP.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                    return;
                }

                if(targetUser.getSymbolType() == SymbolType.SYMBOL_7){
                    player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                    return;
                }

                targetUser.setSymbolType(SymbolType.SYMBOL_7);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                targetUser.save();
            }));

            contents.set(2, 2, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §a§l✦", Arrays.asList(new String[] { symbolLore[0], symbolLore[1], symbolLore[2] })), e -> {
                if(targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.MVIP)) {
                    player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MVIP.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                    return;
                }

                if(targetUser.getSymbolType() == SymbolType.SYMBOL_8){
                    player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                    return;
                }

                targetUser.setSymbolType(SymbolType.SYMBOL_8);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                targetUser.save();
            }));

            contents.set(2, 3, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §a§l❦", Arrays.asList(new String[] { symbolLore[0], symbolLore[1], symbolLore[2] })), e -> {
                if(targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.MVIPPLUS)) {
                    player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MVIPPLUS.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                    return;
                }

                if(targetUser.getSymbolType() == SymbolType.SYMBOL_9){
                    player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                    return;
                }

                targetUser.setSymbolType(SymbolType.SYMBOL_9);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                targetUser.save();
            }));

            contents.set(2, 4, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §a§lE", Arrays.asList(new String[] { symbolLore[0], symbolLore[1], symbolLore[2] })), e -> {
                if(!targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.MVIPPLUS)) {
                    player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MVIPPLUS.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                    return;
                }

                if(targetUser.getSymbolType() == SymbolType.SYMBOL_10){
                    player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                    return;
                }

                targetUser.setSymbolType(SymbolType.SYMBOL_10);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                targetUser.save();
            }));

            contents.set(2, 5, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §e§l❝E❞", Arrays.asList(new String[] { symbolLore[0], symbolLore[1], symbolLore[2] })),  e -> {
                if(targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.CVIP)) {
                    player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.CVIP.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                    return;
                }

                if(targetUser.getSymbolType() == SymbolType.SYMBOL_11){
                    player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                    return;
                }

                targetUser.setSymbolType(SymbolType.SYMBOL_11);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                targetUser.save();
            }));

            contents.set(2, 6, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §e§lPRO", Arrays.asList(new String[] { symbolLore[0], symbolLore[1], symbolLore[2] })),  e -> {
                if(targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.CVIP)) {
                    player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.CVIP.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                    return;
                }

                if(targetUser.getSymbolType() == SymbolType.SYMBOL_12){
                    player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                    return;
                }

                targetUser.setSymbolType(SymbolType.SYMBOL_12);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                targetUser.save();
            }));

            contents.set(3, 3, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §e§lMASTER", Arrays.asList(new String[] { symbolLore[0], symbolLore[1], symbolLore[2] })), e -> {
                if(targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.CVIP)) {
                    player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.CVIP.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                    return;
                }

                if(targetUser.getSymbolType() == SymbolType.SYMBOL_13){
                    player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                    return;
                }

                targetUser.setSymbolType(SymbolType.SYMBOL_13);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                targetUser.save();
            }));

            contents.set(3, 4, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §e§lCRY", Arrays.asList(new String[] { symbolLore[0], symbolLore[1], symbolLore[2] })), e -> {
                if(!targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.CVIPPLUSPLUS)) {
                    player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.CVIPPLUSPLUS.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                    return;
                }

                if(targetUser.getSymbolType() == SymbolType.SYMBOL_14){
                    player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                    return;
                }

                targetUser.setSymbolType(SymbolType.SYMBOL_14);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                targetUser.save();
            }));

            contents.set(3, 5, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §e§lUwU", Arrays.asList(new String[] { symbolLore[0], symbolLore[1], symbolLore[2] })), e -> {
                if(targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.CVIPPLUSPLUS)) {
                    player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.CVIPPLUSPLUS.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                    return;
                }

                if(targetUser.getSymbolType() == SymbolType.SYMBOL_15){
                    player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                    return;
                }

                targetUser.setSymbolType(SymbolType.SYMBOL_15);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                targetUser.save();
            }));

            contents.set(3, 3, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§7Sembol: §d#CLIMPY", Arrays.asList(new String[] { symbolLore[0], symbolLore[1], symbolLore[2] })), e -> {
                if(!targetUser.getSymbolType().getRankType().isAboveOrEqual(RankType.CVIPPLUSPLUS)) {
                    player.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.CVIPPLUSPLUS.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                    return;
                }

                if(targetUser.getSymbolType() == SymbolType.SYMBOL_16){
                    player.sendMessage(ChatColor.RED + targetUser.getSymbolType().getPrefix() + " sembolü şu anda kullanılıyor.");
                    return;
                }

                targetUser.setSymbolType(SymbolType.SYMBOL_16);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYeni sembol seçildi! Gözükecek sembolunuz; &f" + targetUser.getSymbolType().getPrefix()));
                targetUser.save();
            }));
        }
    }
}
