package net.amyls.backups;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import net.amyls.backups.connection.MySQLConnection;
import net.amyls.backups.connection.files.Config;
import net.amyls.backups.connection.stores.Store;
import net.amyls.backups.exec.BackupExec;
import net.amyls.backups.event.BackupEvent;

/**
 * @Author: amyls.
 * @Website: https://420club.xyz/
 * @Discord: amyls.#0004
 *
 * @Create 28.05.2022
 * @Copyright
 */

public class BackupPlugin extends JavaPlugin {
    private static BackupPlugin i;

    private static Store store;

    public static BackupPlugin getInst() {
        return i;
    }

    public static Store getStore() {
        return store;
    }

    public void onEnable() {
        i = this;
        Config.reloadConfig();
        Conn();
        getCommand("backup").setExecutor(new BackupExec());
        getServer().getPluginManager().registerEvents(new BackupEvent(), (Plugin)this);
    }

    protected boolean Conn() {
        store = (Store)new MySQLConnection(Config.DATABASE_MYSQL_HOST, Config.DATABASE_MYSQL_PORT, Config.DATABASE_MYSQL_USER, Config.DATABASE_MYSQL_PASS, Config.DATABASE_MYSQL_NAME, Config.DATABASE_TABLEPREFIX);
        boolean conn = store.connect();
        if (conn) {
            store.update(true, "create table if not exists `{P}backups` (`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,`name` varchar(32) NOT NULL,`time` bigint(22) NOT NULL, `killer` varchar(32) NOT NULL, `ping` int(11) NOT NULL, `inventory` text NOT NULL, `armor` text NOT NULL);");
            return conn;
        }
        return conn;
    }
    }