package org.cathal02.hopperfilter.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.cathal02.hopperfilter.HopperFilter;

import java.io.File;
import java.io.IOException;

public class MessageManager {

    public HopperFilter plugin;
    private File customConfigFile;
    private static FileConfiguration customConfig;

    public MessageManager(HopperFilter plugin)
    {
     this.plugin = plugin;
     createCustomConfig();
    }

    public static void cannotBreakOpenHopper(Player player) {
        player.sendMessage(translate(customConfig.getString("cannotBreakOpenHopper")));

    }

    public static String getHopperSettingsName() {
        return translate(customConfig.getString("openHopperSettings"));

    }

    public static String getItemName(String itemName) {

        return translate(customConfig.getString(itemName));
    }

    private void createCustomConfig() {
        customConfigFile = new File(plugin.getDataFolder(), "lang.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            plugin.saveResource("lang.yml", false);
        }

        customConfig= new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void CannotOpenHopper(Player player)
    {
        player.sendMessage(translate(customConfig.getString("cannotOpenHopper")));
    }

    public static void alreadyFriends(Player player) {
        player.sendMessage(translate(customConfig.getString("alreadyFriends")));

    }

    public static void cannotBeFriendsWithSelf(Player player) {
      player.sendMessage(translate(customConfig.getString("cannotBeFriendsWithSelf")));

    }

    public static void cannotBreakHopper(Player player) {
        player.sendMessage(translate(customConfig.getString("cannotBreakHopper")));

    }

    public static void onlyHopperOwnerCanDoThis(Player player)
    {
        player.sendMessage(translate(customConfig.getString("noPermission")));

    }

    public static void friendAdded(Player player) {
        player.sendMessage(translate(customConfig.getString("friendAdded")));
    }



    private static String translate(String text)
    {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
