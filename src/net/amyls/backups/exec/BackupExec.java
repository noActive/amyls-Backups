package net.amyls.backups.exec;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.amyls.backups.connection.files.Config;
import net.amyls.backups.util.BackupUtil;
import net.amyls.backups.Backup;

/**
 * @Author: amyls.
 * @Website: https://420club.xyz/
 * @Discord: amyls.#0004
 *
 * @Create 28.05.2022
 * @Copyright
 */

public class BackupExec implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player p = (Player)sender;
        if (!sender.hasPermission("amyls.cmd.backup"))
            return BackupUtil.sendMessage(sender, Config.MESSAGES_NOT_PERMISSION);
        if (args.length != 1)
            return BackupUtil.sendMessage(sender, Config.MESSAGES_CORRECTUSAGE);
        Player o = Bukkit.getPlayer(args[0]);
        if (o == null)
            return BackupUtil.sendMessage((CommandSender)p, Config.MESSAGES_OFFLINE);
        Backup.getList(o, p);
        return false;
    }
}
