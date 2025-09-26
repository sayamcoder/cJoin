package com.bx5z.cJoin.util;

import com.bx5z.cJoin.CJoin;
import org.bukkit.Bukkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    private final CJoin plugin;
    private final int resourceId;
    private boolean updateAvailable = false;
    private String latestVersion;

    public UpdateChecker(CJoin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void check() {
        if (!plugin.getConfig().getBoolean("update-checker.enabled", true)) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    latestVersion = scanner.next();
                    if (!plugin.getDescription().getVersion().equalsIgnoreCase(latestVersion)) {
                        updateAvailable = true;
                        plugin.getLogger().info("An update for cJoin is available! (v" + latestVersion + ")");
                        plugin.getLogger().info("Download it from SpigotMC!");
                    }
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Could not check for updates: " + exception.getMessage());
            }
        });
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getLatestVersion() {
        return latestVersion;
    }
}