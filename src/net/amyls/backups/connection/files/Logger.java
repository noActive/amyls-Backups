package net.amyls.backups.connection.files;

import java.util.logging.Level;
import net.amyls.backups.BackupPlugin;

/**
 * @Author: amyls.
 * @Website: https://420club.xyz/
 * @Discord: amyls.#0004
 *
 * @Create 28.05.2022
 * @Copyright
 */

public class Logger {
    public static void log(Level level, String log) {
        BackupPlugin.getInst().getLogger().log(level, log);
    }

    public static void info(String... logs) {
        String[] arrayOfString = logs;
        int i = logs.length;
        for (byte b = 0; b < i; b = (byte)(b + 1)) {
            String s = arrayOfString[b];
            log(Level.INFO, s);
        }
    }

    public static void warning(String... logs) {
        String[] arrayOfString = logs;
        int i = logs.length;
        for (byte b = 0; b < i; b = (byte)(b + 1)) {
            String s = arrayOfString[b];
            log(Level.WARNING, s);
        }
    }
}
