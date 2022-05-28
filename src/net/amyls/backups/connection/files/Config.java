package net.amyls.backups.connection.files;

import java.lang.reflect.Field;
import org.bukkit.configuration.file.FileConfiguration;
import net.amyls.backups.BackupPlugin;

/**
 * @Author: amyls.
 * @Website: https://420club.xyz/
 * @Discord: amyls.#0004
 *
 * @Create 28.05.2022
 * @Copyright
 */

public class Config {
    public static void loadConfig() {
        try {
            BackupPlugin.getInst().saveDefaultConfig();
            FileConfiguration c = BackupPlugin.getInst().getConfig();
            Field[] fields;
            for (int length = (fields = Config.class.getFields()).length, i = 0; i < length; i++) {
                Field f = fields[i];
                if (c.isSet("config." + f.getName().toLowerCase().replace("_", ".")))
                    f.set(null, c.get("config." + f.getName().toLowerCase().replace("_", ".")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            FileConfiguration c = BackupPlugin.getInst().getConfig();
            Field[] fields;
            for (int length = (fields = Config.class.getFields()).length, i = 0; i < length; i++) {
                Field f = fields[i];
                c.set("config." + f.getName().toLowerCase().replace("_", "."), f.get(null));
            }
            BackupPlugin.getInst().saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reloadConfig() {
        BackupPlugin.getInst().reloadConfig();
        loadConfig();
        saveConfig();
    }

    public static String DATABASE_TABLEPREFIX = "amylsToKociak_";

    public static String DATABASE_MYSQL_HOST = "nokurwalocalhostaco";

    public static int DATABASE_MYSQL_PORT = 3306;

    public static String DATABASE_MYSQL_USER = "kurwaroot";

    public static String DATABASE_MYSQL_PASS = "";

    public static String DATABASE_MYSQL_NAME = "backups";

    public static String MESSAGES_NOT_PERMISSION = "&cNie posiadasz dostepu do komendy!";

    public static String MESSAGES_CORRECTUSAGE = "&cPoprawne uzycie komendy: &4/backup <nick gracza>";

    public static String MESSAGES_OFFLINE = "&cPodany skurwesyn jest offilne";
}
