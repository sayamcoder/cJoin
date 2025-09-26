package com.bx5z.cJoin.listeners;

import com.bx5z.cJoin.CJoin;
import com.bx5z.cJoin.api.events.CJoinMessageEvent;
import com.bx5z.cJoin.api.events.CQuitMessageEvent;
import com.bx5z.cJoin.util.MessageHandler;
import com.bx5z.cJoin.util.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    private final CJoin plugin;

    public PlayerJoinQuitListener(CJoin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Hide vanilla message
        event.joinMessage(null);

        // Check for silent join permission
        if (player.hasPermission("cjoin.silent")) {
            return;
        }

        // Check if join messages are enabled
        if (!plugin.getConfig().getBoolean("messages.join.enabled", true)) {
            return;
        }

        // Get the resolved message string from config
        String baseMessage = getMessageForPlayer(player, "join");
        String formattedMessage = MessageHandler.format(player, baseMessage);

        // --- API EVENT HOOK ---
        CJoinMessageEvent cJoinEvent = new CJoinMessageEvent(player, formattedMessage);
        Bukkit.getPluginManager().callEvent(cJoinEvent);

        if (cJoinEvent.isCancelled()) {
            return;
        }

        String finalMessage = cJoinEvent.getJoinMessage();

        if (finalMessage != null && !finalMessage.isEmpty()) {
            // Check for and play animation, otherwise send a standard message
            plugin.getAnimationHandler().playAnimation(finalMessage, "join");
        }

        // --- UPDATE CHECKER NOTIFICATION ---
        UpdateChecker checker = plugin.getUpdateChecker();
        if (player.hasPermission("cjoin.admin") && checker != null && checker.isUpdateAvailable()) {
            // Small delay to ensure the message isn't lost in the join spam
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                String prefix = ChatColor.translateAlternateColorCodes('&', MessageHandler.getMessage("plugin-messages.prefix"));
                String updateText = MessageHandler.getMessage("plugin-messages.update-available")
                        .replace("{new_version}", checker.getLatestVersion());
                player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', updateText));
            }, 20L); // 20 ticks = 1 second
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Hide vanilla message
        event.quitMessage(null);

        // Check for silent quit permission
        if (player.hasPermission("cjoin.silent")) {
            return;
        }

        // Check if quit messages are enabled
        if (!plugin.getConfig().getBoolean("messages.quit.enabled", true)) {
            return;
        }

        String baseMessage = getMessageForPlayer(player, "quit");
        String formattedMessage = MessageHandler.format(player, baseMessage);

        // --- API EVENT HOOK ---
        CQuitMessageEvent cQuitEvent = new CQuitMessageEvent(player, formattedMessage);
        Bukkit.getPluginManager().callEvent(cQuitEvent);

        if (cQuitEvent.isCancelled()) {
            return;
        }

        String finalMessage = cQuitEvent.getQuitMessage();

        if (finalMessage != null && !finalMessage.isEmpty()) {
            plugin.getAnimationHandler().playAnimation(finalMessage, "quit");
        }
    }

    private String getMessageForPlayer(Player player, String eventType) {
        // 1. First Join (only for "join" event)
        if (eventType.equals("join") && plugin.getConfig().getBoolean("first-join.enabled") && !player.hasPlayedBefore()) {
            return plugin.getConfig().getString("first-join.message");
        }

        // 2. World-specific message
        String worldName = player.getWorld().getName();
        String worldMessagePath = "worlds." + worldName + "." + eventType;
        if (plugin.getConfig().isSet(worldMessagePath)) {
            String worldMessage = plugin.getConfig().getString(worldMessagePath);
            if (worldMessage != null && !worldMessage.isEmpty()) {
                return worldMessage;
            }
        }

        // 3. Permission group message (in order from config)
        ConfigurationSection groups = plugin.getConfig().getConfigurationSection("permission-groups");
        if (groups != null) {
            for (String groupKey : groups.getKeys(false)) {
                if (player.hasPermission("cjoin." + groupKey)) {
                    String groupMessagePath = "permission-groups." + groupKey + "." + eventType;
                    String groupMessage = plugin.getConfig().getString(groupMessagePath);
                    if (groupMessage != null && !groupMessage.isEmpty()) {
                        return groupMessage;
                    }
                }
            }
        }

        // 4. Default message
        return plugin.getConfig().getString("default." + eventType);
    }
}