package org.cathal02.hopperfilter.data;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HopperData {

    public HopperData(Location hopperLocation, ItemStack[] items, Integer allowedItems, List<String> friends, boolean isPublic, String owner, boolean blacklistEnabled, boolean advancedFilter)
    {
        this.hopperItems = items;
        this.location = hopperLocation;
        this.allowedItems = allowedItems;
        this.friends = friends;
        this.isPublic = isPublic;
        this.owner = owner;
        this.blacklistEnabled = blacklistEnabled;
        this.advancedFilter = advancedFilter;
    }

    public ItemStack[] hopperItems;
    public List<String> friends;
    public boolean isPublic = true;
    public Location location;
    public int allowedItems = 0;
    public String owner;
    public boolean blacklistEnabled;
    public boolean advancedFilter;

    public boolean allowedOpenHopper(Player player) {
        if(owner.equalsIgnoreCase(player.getUniqueId().toString()))return true;
        if(player.isOp())return true;
        if(isPublic)return true;
        return  friends.contains(player.getUniqueId().toString());
    }
}
