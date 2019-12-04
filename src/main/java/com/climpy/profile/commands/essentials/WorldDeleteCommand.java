package com.climpy.profile.commands.essentials;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.profile.utils.C;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class WorldDeleteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                return true;
            }
        }

        if(args.length < 1) {
            player.sendMessage(C.color("&cDoğru Kullanım: /" + label + " <dünya-Adı..>"));
            return true;
        }

        String worldName = "";
        for (String arg : args)
            worldName = arg;
        player.sendMessage(C.color("&f\"" + worldName + "\"" + " &6ismine sahip dünya siliniyor.."));
        World world = Bukkit.createWorld(WorldCreator.name(worldName));
        File file = world.getWorldFolder();
        Bukkit.unloadWorld(world, false);
        delete(file);
        player.sendMessage(C.color("&f\"" + worldName + "\"" + " &6ismine sahip dünya silindi."));
       // player.teleport(new Location(world, 0.0D, 80.0D, 0.0D));
        return true;
    }

    public static void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int index = 0; index < files.length; index++) delete(files[index]);
        }
        file.delete();
    }
}
