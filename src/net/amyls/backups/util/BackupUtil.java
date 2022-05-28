package net.amyls.backups.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.bind.DatatypeConverter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @Author: amyls.
 * @Website: https://420club.xyz/
 * @Discord: amyls.#0004
 *
 * @Create 28.05.2022
 * @Copyright
 */

public class BackupUtil {
    public static String fixColor(String s) {
        if (s == null)
            return "";
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String itemsToString(ItemStack[] items) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(serializeItemStack(items));
            oos.flush();
            return DatatypeConverter.printBase64Binary(bos.toByteArray());
        } catch (Exception e) {
            return "";
        }
    }

    public static ItemStack[] stringToItems(String s) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(s));
            ObjectInputStream ois = new ObjectInputStream(bis);
            return deserializeItemStack((Map<String, Object>[])ois.readObject());
        } catch (Exception e) {
            return new ItemStack[] { new ItemStack(Material.AIR) };
        }
    }

    private static Map<String, Object>[] serializeItemStack(ItemStack[] items) {
        Map[] result = new Map[items.length];
        for (int i = 0; i < items.length; i++) {
            ItemStack is = items[i];
            if (is == null) {
                result[i] = new HashMap<>();
            } else {
                result[i] = is.serialize();
                if (is.hasItemMeta())
                    result[i].put("meta", is.getItemMeta().serialize());
            }
        }
        return (Map<String, Object>[])result;
    }

    private static ItemStack[] deserializeItemStack(Map<String, Object>[] map) {
        ItemStack[] items = new ItemStack[map.length];
        for (int i = 0; i < items.length; i++) {
            Map<String, Object> s = map[i];
            if (s.size() == 0) {
                items[i] = null;
            } else {
                try {
                    if (s.containsKey("meta")) {
                        Map<String, Object> im = new HashMap<>((Map<? extends String, ?>)s.remove("meta"));
                        im.put("==", "ItemMeta");
                        ItemStack is = ItemStack.deserialize(s);
                        is.setItemMeta((ItemMeta)ConfigurationSerialization.deserializeObject(im));
                        items[i] = is;
                    } else {
                        items[i] = ItemStack.deserialize(s);
                    }
                } catch (Exception e) {
                    items[i] = null;
                }
            }
        }
        return items;
    }

    public static boolean sendMessage(CommandSender sender, String message) {
        if (sender instanceof org.bukkit.entity.Player) {
            if (message != null || message != "")
                sender.sendMessage(fixColor(message));
        } else {
            sender.sendMessage(ChatColor.stripColor(fixColor(message)));
        }
        return false;
    }

    public static String getDate(long time) {
        return dateFormat.format(new Date(time));
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");

    private static final LinkedHashMap<Integer, String> values;

    static {
        (values = new LinkedHashMap<>(6)).put(Integer.valueOf(31104000), "y");
        values.put(Integer.valueOf(2592000), "msc");
        values.put(Integer.valueOf(86400), "d");
        values.put(Integer.valueOf(3600), "h");
        values.put(Integer.valueOf(60), "min");
        values.put(Integer.valueOf(1), "sek");
    }
}
