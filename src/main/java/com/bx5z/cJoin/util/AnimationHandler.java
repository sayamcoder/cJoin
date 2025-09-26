package com.bx5z.cJoin.util;

import com.bx5z.cJoin.CJoin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.List;

public class AnimationHandler {

    private final CJoin plugin;

    public AnimationHandler(CJoin plugin) {
        this.plugin = plugin;
    }

    public void playAnimation(String message, String animationType) {
        if (!plugin.getConfig().getBoolean("animations." + animationType + ".enabled", false)) {
            Bukkit.broadcastMessage(message);
            return;
        }

        List<String> frames = plugin.getConfig().getStringList("animations." + animationType + ".frames");
        long interval = plugin.getConfig().getLong("animations." + animationType + ".interval-ticks", 3L);

        if (frames.isEmpty()) {
            Bukkit.broadcastMessage(message); // Fallback if no frames are defined
            return;
        }

        new BukkitRunnable() {
            private int frameIndex = 0;

            @Override
            public void run() {
                if (frameIndex >= frames.size()) {
                    this.cancel();
                    return;
                }

                String frame = frames.get(frameIndex);
                // Strip colors from the original message to inherit frame colors
                String plainMessage = ChatColor.stripColor(message);
                String animatedMessage = ChatColor.translateAlternateColorCodes('&', frame.replace("{message}", plainMessage));

                Bukkit.broadcastMessage(animatedMessage);
                frameIndex++;
            }
        }.runTaskTimer(plugin, 0L, interval);
    }
}