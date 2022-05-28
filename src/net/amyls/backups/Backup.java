package net.amyls.backups;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.amyls.backups.connection.files.Logger;
import net.amyls.backups.connection.stores.Callback;
import net.amyls.backups.util.BackupUtil;
import net.amyls.backups.BackupPlugin;
import net.amyls.backups.event.BackupEvent;
import net.amyls.backups.exec.BackupExec;

/**
 * @Author: amyls.
 * @Website: https://420club.xyz/
 * @Discord: amyls.#0004
 *
 * @Create 28.05.2022
 * @Copyright
 */

public class Backup {

    private String name;
    private int ping;
    private long time;
    private ItemStack[] inventory;
    private ItemStack[] armor;
    private static Backup i;
    private String killer;

    public static Backup getInstance() {
        return i;
    }

    public Backup(Player player, String killer) {
        i = this;
        this.name = player.getName();
        this.inventory = player.getInventory().getContents();
        this.armor = player.getInventory().getArmorContents();
        this.time = System.currentTimeMillis();
        this.killer = killer;
        insert();
    }

    private  void insert() {
        BackupPlugin.getStore().update(false, "INSERT INTO `{P}backups`(`id`, `name`, `time`, `killer`, `ping`, `inventory`, `armor`) VALUES (NULL, '" + getName() + "','" + getTime() + "','" + getKiller() + "','" + getPing() + "','" + BackupUtil.itemsToString(getInventory()) + "','" + BackupUtil.itemsToString(getArmor()) + "');");
    }

    public long getTime() {
        return this.time;
    }

    public String getName() {
        return this.name;
    }

    public ItemStack[] getInventory(){
        return this.inventory;
    }

    public String getKiller() {
        return this.killer;
    }

    public long getPing() {
        return this.ping;
    }

    public ItemStack[] getArmor() {
        return this.armor;
    }

    public static void restore(final Player p, final long time, final Player o) {
        BackupPlugin.getStore().query("SELECT *, abs(" + time + " - time) as delta FROM `{P}backups` WHERE `name` = '" + p.getName() + "' ORDER BY delta LIMIT 1", new Callback<ResultSet>() {
            public ResultSet done(ResultSet rs) {
                try {
                    if (rs.next()) {
                        p.getInventory().setArmorContents(BackupUtil.stringToItems(rs.getString("armor")));
                        p.getInventory().setContents(BackupUtil.stringToItems(rs.getString("inventory")));
                        BackupUtil.sendMessage((CommandSender)p, "&7Otrzymano backup z dnia: &9" + BackupUtil.getDate(time));
                        BackupUtil.sendMessage((CommandSender)o, "&7Gracz &9" + p.getName() + "&7 otrzymal backup!");
                    }
                    return null;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void error(Throwable p0) {
                Logger.warning(new String[] { "Error when restore `backup` ", p0.getMessage() });
            }
        });
    }

    public static void getList(final Player p, final Player o) {
        BackupPlugin.getStore().query("SELECT * FROM `{P}backups` WHERE name ='" + p.getName() + "' ORDER BY `id` DESC LIMIT 27;", new Callback<ResultSet>() {
            public ResultSet done(ResultSet rs) {
                try {
                    Inventory inventory = Bukkit.createInventory((InventoryHolder)p, 54, BackupUtil.fixColor("&9&lMENU"));
                    while (rs.next()) {
                        String player = rs.getString("name");
                        long time = rs.getLong("time");
                        int ping = rs.getInt("ping");
                        String killer = rs.getString("killer");
                        ItemStack s = new ItemStack(Material.BOOK_AND_QUILL);
                        ItemMeta customMeta = s.getItemMeta();
                        customMeta.addEnchant(Enchantment.DURABILITY, 10, true);
                        customMeta.setDisplayName(BackupUtil.fixColor("&9ID&8: &f" + time));
                        List<String> lore = new ArrayList<>();
                        lore.add(BackupUtil.fixColor("&9&lGracz: &f" + player));
                        lore.add(BackupUtil.fixColor("&9&l&7Ping: &f" + ping));
                        lore.add(BackupUtil.fixColor("&9&l&7Zabojca: &f" + killer));
                        lore.add(BackupUtil.fixColor("&9&l&7Data i czas: &f" + BackupUtil.getDate(time)));
                        lore.add(BackupUtil.fixColor("&9&l&7Unikalne ID: &f" + time));
                        customMeta.setLore(lore);
                        s.setItemMeta(customMeta);
                        inventory.addItem(new ItemStack[] { s });
                    }
                    o.openInventory(inventory);
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public void error(Throwable p0) {
                Logger.warning(new String[] { "Error when get `backup` ", p0.getMessage() });
            }
        });
    }
}

