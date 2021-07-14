package org.cathal02.hopperfilter.data;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.cathal02.hopperfilter.HopperFilter;
import org.cathal02.hopperfilter.managers.HopperManager;

import java.util.ArrayList;

public class HopperUtils {

    HopperManager hopperManager;
    HopperFilter plugin;
    public HopperUtils(HopperManager manager, HopperFilter plugin)
    {
        this.plugin = plugin;
        hopperManager = manager;
    }

    public Integer getAllowedItems(Inventory inventory, Player player)
    {
        Location hopperLoc = hopperManager.getHopperLocations().get(player.getUniqueId());

        if(hopperLoc != null && hopperManager.hoppers.containsKey(hopperLoc))
        {
            return hopperManager.hoppers.get(hopperLoc).allowedItems;
        }
        else
        {
            return plugin.getUtils().getAllowedFilterSize(player);
        }
    }

    public HopperData getHopperData(Player p)
    {
        if(hopperManager.getHopperLocations().containsKey(p.getUniqueId()))
        {
            if(hopperManager.hoppers.containsKey(hopperManager.getHopperLocations().get(p.getUniqueId())))
            {
                return hopperManager.hoppers.get(hopperManager.getHopperLocations().get(p.getUniqueId()));
            }
        }
        return null;
    }

    public HopperData initHopperData(Player p, Location loc)
    {
        HopperData hopperData;
        if (hopperManager.hoppers.containsKey(loc)) {
            return hopperManager.hoppers.get(loc);
        }
        else
        {
            int allowedItems = plugin.getUtils().getAllowedFilterSize(p);
            hopperData=new HopperData(loc,null, allowedItems, new ArrayList<String>(), true, p.getUniqueId().toString(), false, true);
        }

        hopperManager.hoppers.put(loc, hopperData);
        return hopperData;
    }

    public boolean cancelItemTransferEvent(Location loc, ItemStack item)
    {
        if(!hopperManager.hoppers.containsKey(loc)) return false;
        HopperData hopperData = hopperManager.hoppers.get(loc);
        ItemStack[] filterItems = hopperData.hopperItems;
        if(filterItems == null) return false;

        boolean cancelEvent = false;
        for(int i = 0; i < filterItems.length; i++)
        {
            if(filterItems[i] != null)
            {
                cancelEvent = true;
                if(hopperData.advancedFilter)
                {
                    if(filterItems[i].isSimilar(item))
                    {
                        return hopperData.blacklistEnabled;
                    }
                }
                else
                {
                    if(filterItems[i].getType().equals(item.getType())) return hopperData.blacklistEnabled;
                }

            }
        }

        if(hopperData.blacklistEnabled)
        {
            return !cancelEvent;
        }
        return cancelEvent;

    }

    public void updateHopperIsPublic(Player player, boolean newState)
    {
        HopperData data = hopperManager.getHopperData(player);
        if(data != null)
        {
            data.isPublic = newState;
            hopperManager.hoppers.put(data.location, data);
        }
    }


    public void removeFriend(Player owner, String player) {
        HopperData data = hopperManager.getHopperData(owner);
        if(data != null)
        {
            data.friends.remove(player);
            hopperManager.hoppers.put(data.location, data);
        }
    }

    public void toggleAdvancedFilter(HopperData data) {
        data.advancedFilter = !data.advancedFilter;
    }

    public void dropItems(HopperData hopperData) {
        Location loc = hopperData.location;
        World world =loc.getWorld();
        for(ItemStack item : hopperData.hopperItems)
        {
            if(item != null)
            {
                world.dropItem(loc, item);
            }
        }
    }
}
