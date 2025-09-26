package com.bx5z.cJoin.util;

import com.bx5z.cJoin.CJoin;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageHandler {

    public static String format(Player player, String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }

        // Internal Placeholders
        message = message.replace("{player}", player.getName());
        message = message.replace("{displayname}", player.getDisplayName());
        message = message.replace("{world}", player.getWorld().getName());
        message = message.replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()));
        message = message.replace("{max}", String.valueOf(Bukkit.getMaxPlayers()));

        // PlaceholderAPI Support
        if (CJoin.getInstance().isPlaceholderApiEnabled()) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(CommandSender sender, String path) {
        String message = CJoin.getInstance().getConfig().getString("plugin-messages." + path, "&cMessage not found: " + path);
        String prefix = CJoin.getInstance().getConfig().getString("plugin-messages.prefix", "");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

    public static String getMessage(String path) {
        return CJoin.getInstance().getConfig().getString(path, "");
    }

    // Call this if you add any caching to this class in the future
    public static void reload() {
        // Clear caches if any
    }
}