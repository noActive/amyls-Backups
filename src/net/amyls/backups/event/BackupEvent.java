package net.amyls.backups.event;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.amyls.backups.connection.files.Config;
import net.amyls.backups.Backup;
import net.amyls.backups.BackupPlugin;
import net.amyls.backups.util.BackupUtil;

/**
 * @Author: amyls.
 * @Website: https://420club.xyz/
 * @Discord: amyls.#0004
 *
 * @Create 28.05.2022
 * @Copyright
 */

public class BackupEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void on(PlayerDeathEvent eve) {
        Player p = eve.getEntity();
        Player k = p.getKiller();
        if (k == null) {
            new Backup(p, "null");
            return;
        }
        new Backup(p, k.getName());
    }

    @EventHandler
    public boolean on(InventoryClickEvent e) {
        if (e.getInventory().getName().equals(BackupUtil.fixColor("&6&lMENU BACKUPOW"))) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            Inventory inventory = e.getInventory();
            ItemStack is = e.getCurrentItem();
            if (inventory != null) {
                if (is == null || !is.hasItemMeta() || is.getItemMeta().getDisplayName() == null)
                    return false;
                String name = ((String)is.getItemMeta().getLore().get(0)).substring(15);
                long time = Long.parseLong(is.getItemMeta().getDisplayName().substring(10));
                Player o = Bukkit.getPlayer(name);
                Player p = (Player)e.getWhoClicked();
                if (!o.isOnline())
                    return BackupUtil.sendMessage((CommandSender)p, Config.MESSAGES_OFFLINE);
                Backup.restore(o, time, p);
            }
        }
        return false;
    }
}
