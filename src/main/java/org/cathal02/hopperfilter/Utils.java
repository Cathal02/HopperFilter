package org.cathal02.hopperfilter;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.graalvm.compiler.loop.LoopFragmentWhole;
import sun.security.krb5.Config;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    HopperFilter plugin;
    ItemStack glass;
    Map<String, Integer> hopperSizes = new HashMap<>();
    public Utils(HopperFilter plugin)
    {
        this.plugin = plugin;
        initPermission();


    }

    private void initPermission() {
        if(!plugin.getConfig().contains("hopperSizes"))return;

        ConfigurationSection hopperSizesSection = plugin.getConfig().getConfigurationSection("hopperSizes");
        for(String permission : hopperSizesSection.getKeys(false))
        {
            hopperSizes.put(permission, hopperSizesSection.getInt(permission));
        }
    }

    public Integer getInventorySize(Integer configSize)
    {
        if(configSize <=9) return 18;
        else if(configSize<=18) return 27;
        else if(configSize <= 27) return 36;
        else if(configSize<=36) return 45;
        return 54;
    }

    public Integer getAllowedFilterSize(Player player)
    {
        for(String perm : hopperSizes.keySet())
        {
            if(player.hasPermission("hopperFilter.use." + perm))
            {
                return hopperSizes.get(perm);
            }
        }
        return 9;
    }



}
