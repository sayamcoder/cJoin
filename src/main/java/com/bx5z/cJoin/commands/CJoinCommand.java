package com.bx5z.cJoin.commands;

import com.bx5z.cJoin.CJoin;
import com.bx5z.cJoin.util.MessageHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CJoinCommand implements CommandExecutor, TabCompleter {

    private final CJoin plugin;

    public CJoinCommand(CJoin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("cjoin.admin")) {
            MessageHandler.sendMessage(sender, "no-permission");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelpMessage(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "reload":
                handleReload(sender);
                break;
            case "preview":
                handlePreview(sender, args);
                break;
            default:
                MessageHandler.sendMessage(sender, "invalid-command");
                break;
        }
        return true;
    }

    private void handleReload(CommandSender sender) {
        plugin.reloadPluginConfig();
        MessageHandler.sendMessage(sender, "reload");
    }

    private void handlePreview(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return;
        }

        if (args.length < 2) {
            MessageHandler.sendMessage(sender, "preview-usage");
            return;
        }

        Player player = (Player) sender;
        String type = args[1].toLowerCase();
        String messagePath = null;

        switch (type) {
            case "join":
            case "quit":
                if (args.length > 2) { // e.g., /cjoin preview join staff
                    String group = args[2].toLowerCase();
                    messagePath = "permission-groups." + group + "." + type;
                } else {
                    messagePath = "default." + type;
                }
                break;
            case "first":
                messagePath = "first-join.message";
                break;
            default:
                MessageHandler.sendMessage(sender, "preview-usage");
                return;
        }

        String message = MessageHandler.getMessage(messagePath);

        if (message.isEmpty()) {
            MessageHandler.sendMessage(sender, "preview-not-found");
            return;
        }

        String header = ChatColor.translateAlternateColorCodes('&', MessageHandler.getMessage("plugin-messages.preview-header"));
        sender.sendMessage(header);
        sender.sendMessage(MessageHandler.format(player, message));
    }

    // ======== FIX IS IN THIS METHOD ========
    private void sendHelpMessage(CommandSender sender) {
        // Get the raw prefix from the config
        String prefix = MessageHandler.getMessage("plugin-messages.prefix");

        // Translate colors for all messages before sending
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&bAvailable Commands:"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/cjoin reload &7- Reloads the configuration file."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/cjoin preview <join|quit|first> [group] &7- Previews a message."));
    }
    // =======================================


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!sender.hasPermission("cjoin.admin")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            return Arrays.asList("reload", "preview", "help").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("preview")) {
            return Arrays.asList("join", "quit", "first").stream()
                    .filter(s -> s.startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("preview") && (args[1].equalsIgnoreCase("join") || args[1].equalsIgnoreCase("quit"))) {
            if (plugin.getConfig().getConfigurationSection("permission-groups") != null) {
                return plugin.getConfig().getConfigurationSection("permission-groups").getKeys(false).stream()
                        .filter(s -> s.startsWith(args[2].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }

        return new ArrayList<>();
    }
}