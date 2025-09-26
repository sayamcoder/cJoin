package com.bx5z.cJoin;

import com.bx5z.cJoin.commands.CJoinCommand;
import com.bx5z.cJoin.listeners.PlayerJoinQuitListener;
import com.bx5z.cJoin.util.AnimationHandler;
import com.bx5z.cJoin.util.MessageHandler;
import com.bx5z.cJoin.util.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CJoin extends JavaPlugin {

    private static CJoin instance;
    private boolean placeholderApiEnabled = false;
    private UpdateChecker updateChecker;
    private AnimationHandler animationHandler;

    @Override
    public void onEnable() {
        instance = this;

        // Configuration
        saveDefaultConfig();

        // PlaceholderAPI Check
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderApiEnabled = true;
            getLogger().info("PlaceholderAPI found! Enabling integration.");
        } else {
            getLogger().info("PlaceholderAPI not found. Some placeholders will not be available.");
        }

        // Initialize new handlers
        this.animationHandler = new AnimationHandler(this);
        int resourceId = getConfig().getInt("update-checker.spigot-resource-id", 0);
        if (resourceId != 0 && resourceId != 99999) { // Don't check with the placeholder ID
            this.updateChecker = new UpdateChecker(this, resourceId);
            this.updateChecker.check();
        } else {
            getLogger().info("Update checker is disabled or using a placeholder ID.");
        }

        // Register Listeners and Commands
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
        getCommand("cjoin").setExecutor(new CJoinCommand(this));

        getLogger().info("cJoin has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("cJoin has been disabled.");
    }

    public void reloadPluginConfig() {
        reloadConfig();
        MessageHandler.reload(); // If you add caching later
    }

    public static CJoin getInstance() {
        return instance;
    }

    public boolean isPlaceholderApiEnabled() {
        return placeholderApiEnabled;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public AnimationHandler getAnimationHandler() {
        return animationHandler;
    }
}