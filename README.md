# cJoin

[![Spigot Version](https://img.shields.io/badge/Spigot-1.1.0-orange)](https://www.spigotmc.org/resources/cjoin-modern-join-quit-messages-1-21.129091/)
[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21-green)](https://papermc.io/)
[![Build Status](https://github.com/sayamcoder/cJoin/actions/workflows/build.yml/badge.svg)](https://github.com/YOUR_USERNAME/cJoin/actions)
[![License](https://img.shields.io/badge/License-MIT-blue)](./LICENSE)

Modern Join, Quit, and First-Join Messages for PaperMC.

**cJoin** is a powerful, lightweight, and feature-rich plugin for PaperMC servers that replaces default connection messages with a fully customizable system. It's built for performance and designed to be easy for server owners to configure and for developers to extend.


*(Optional: Replace with a screenshot or GIF of the plugin in action)*

---

## ✨ Features

-   **Custom Messages:** Full control over join, quit, and first-time join messages with color code support.
-   **Permission-Based Groups:** Assign unique messages to different player ranks (e.g., `cjoin.vip`, `cjoin.staff`).
-   **Per-World Overrides:** Display different messages based on the world a player connects to or disconnects from.
-   **Lag-Free Animations:** Create beautiful, animated connection messages with a simple frame-based system.
-   **Placeholder Support:** Use built-in placeholders (`{player}`, `{world}`, etc.) and full **PlaceholderAPI** integration.
-   **Silent Mode:** A `cjoin.silent` permission allows staff to join/leave without broadcasting messages.
-   **Developer API:** A clean, event-based API to allow other plugins to interact with cJoin's messages.
-   **Update Checker:** Automatically notifies admins of new plugin versions.

---

## 🚀 Installation

1.  Download the latest version from the [SpigotMC page](https://www.spigotmc.org/resources/cjoin-modern-join-quit-messages.YOUR_ID_HERE/) or [GitHub Releases](https://github.com/YOUR_USERNAME/cJoin/releases).
2.  Place the downloaded `.jar` file into your server's `/plugins` directory.
3.  Start or restart your server to generate the configuration file.
4.  Customize the messages and settings in `plugins/cJoin/config.yml`.
5.  Use `/cjoin reload` to apply your changes.

---

## ⚙️ Configuration

The `config.yml` is designed to be intuitive. Here are some key sections:

```yaml
# Permission-based messages
permission-groups:
  staff:
    join: "&b⚡ {player} (Staff) has arrived!"
    quit: "&b⚡ {player} (Staff) has departed."
  vip:
    join: "&d⭐ {player} (VIP) has entered the server!"
    quit: "&d⭐ {player} (VIP) has left the server."

# Per-world messages
worlds:
  Hub:
    join: "&e{player} joined the Hub!"
    quit: "&e{player} left the Hub."

# Animations
animations:
  join:
    enabled: true
    interval-ticks: 3 # 20 ticks = 1 second
    frames:
      - "&a&l> &a{message}"
      - "&a&l>> &2{message}"
      - "&a&l>>> &a{message}"
```

---

## 📋 Commands & Permissions

| Command                                           | Permission      | Description                                |
| ------------------------------------------------- | --------------- | ------------------------------------------ |
| `/cjoin reload`                                   | `cjoin.admin`   | Reloads the configuration from disk.       |
| `/cjoin preview <join\|quit\|first> [group]` | `cjoin.admin`   | Previews a message format in-game.         |
| `/cjoin help`                                     | `cjoin.admin`   | Displays the command help menu.            |
| *(Silent Join/Leave)*                             | `cjoin.silent`  | Hides connection messages for the player.  |
| *(Custom Group Messages)*                        | `cjoin.<group>` | Displays messages for a specific group.    |

---

## 👩‍💻 For Developers

### API Events

cJoin provides two custom Bukkit events for deep integration.

-   `CJoinMessageEvent`: Fired before a join message is broadcast.
-   `CQuitMessageEvent`: Fired before a quit message is broadcast.

You can listen to these events to read, modify, or cancel a message broadcast.

**Example Usage:**

```java
import com.bx5z.cJoin.api.events.CJoinMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MyListener implements Listener {

    @EventHandler
    public void onCJoinMessage(CJoinMessageEvent event) {
        // Add a prefix to all join messages
        String originalMessage = event.getJoinMessage();
        event.setJoinMessage("§8[§a+§8] §r" + originalMessage);

        // Cancel the message if the player's name is "Notch"
        if (event.getPlayer().getName().equalsIgnoreCase("Notch")) {
            event.setCancelled(true);
        }
    }
}
```

### Building from Source

To build cJoin from the source code, you'll need:

-   Java Development Kit (JDK) 17 or newer
-   Apache Maven

Clone the repository and run the following command from the project's root directory:

```bash
mvn clean package
```

The compiled JAR will be located in the `target` directory.

---

## 🤝 Contributing

Contributions are welcome! If you'd like to contribute, please follow these steps:

1.  **Fork** the repository.
2.  Create a new **branch** for your feature or bug fix.
3.  Make your changes and commit them with a clear message.
4.  Submit a **Pull Request** with a detailed description of your changes.

---
